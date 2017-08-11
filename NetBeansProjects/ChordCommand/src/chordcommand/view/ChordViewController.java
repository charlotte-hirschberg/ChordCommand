package chordcommand.view;

import chordcommand.ChordCommand;
import chordcommand.util.ChordUtil;
import chordcommand.model.Chord;
import chordcommand.model.MajorKey;
import chordcommand.model.Scale;
import chordcommand.util.AlertSetter;
import java.sql.SQLException;
import java.text.Normalizer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;

/** 
 * @Course: SDEV 435 ~ Applied Software Practice
 * @Author Name: Charlotte Hirschberger
 * @Assignment ChordCommand
 * @Date: Jun 12, 2017
 * @Description: The ChordViewController class interacts with the ChordView.fxml
 * file, obtaining input from some controls and setting content in others. This
 * controller is responsible for the buttons, combo box, and text fields where a
 * chord symbol is entered and the panes where output appears, including for the
 * piano.
 */
public class ChordViewController extends Controller {

    @FXML
    private TextField keyTF;    // uneditable textfield, filled via ComboBox
    @FXML
    private TextField symbolTF;
    @FXML
    private ComboBox<String> instrCombo;    // instruments for transposition
    @FXML
    private ComboBox<String> keyCombo;
    @FXML
    private Label entryLbl;                 // Output heading
    @FXML
    private Label scalesLbl;                // Scales pane title
    @FXML 
    private Label instrComboLbl;
    @FXML
    private TextArea chordTA;               // Chord output pane
    @FXML
    private TreeView scaleTree;             // Scale output
    @FXML
    private Pane pianoPane;                 // Pane of rectangles
    @FXML
    private Label errorMsg;
    
    private ChordUtil cu;
    private ArrayList<KeyMarker> markers;   // Circles & labels to mark keys
    private ObservableList<Chord> recentChords;
    
    private RootLayoutController rootCtrl;
    private AlertSetter alerter;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. It sets ComboBox handling
     * and prepares the lists that hold Chords and KeyMarkers.
     */
    @FXML
    private void initialize() 
    {
        recentChords = FXCollections.observableArrayList();
        instrCombo.setDisable(true);
        instrComboLbl.setDisable(true);
        markers = new ArrayList();
        keyTF.setEditable(false);
        alerter = new AlertSetter();
        
        keyCombo.setOnAction((event) -> 
        {
            String selectedKey = keyCombo.getSelectionModel().getSelectedItem();
            keyTF.setText(selectedKey);
        });
        
        instrCombo.setOnAction((event)->
        {
            String selectedInstr = keyCombo.getSelectionModel().getSelectedItem();
            transpose(selectedInstr);
        });
    }

    /**
     * Calls setCombo to populate ComboBoxes with data from ObservableLists.
     */
    public void setCombos()
    {
         setCombo(instrCombo, "Flute", ((ChordCommand)mainApp).getInstrComboData());
         setCombo(keyCombo, "C", ((ChordCommand)mainApp).getKeyComboData());
    }
    
    /**
     * Fill a ComboBox with the indicated data and set its title.
     * @param combo the ComboBox to fill
     * @param title the ComboBox's visible value
     * @param data an ObservableList
     */
    private void setCombo(ComboBox combo, String title, ObservableList<String> data)
    {
        combo.setItems(data);
        combo.setValue(title);
    }
    
    /**
     * Give this controller a reference to RootLayoutController, for purposes of
     * setting Recent Chords in a ListView.
     * @param ctrller the RootLayoutController
     */
    public void setRootCtrl(RootLayoutController ctrller)
    {
        rootCtrl = ctrller;
    }
    
    /**
     * Hide piano and scale panes if necessary, according to user preferences
     */
    public void initOutputArea()
    {
        // If showAnyScale = false, hide scaleTree and its label
        hideScales();
        
        // If pianoPane = false, hide its label
        if(!((ChordCommand)mainApp).getBooleanProp("showPiano"))
            pianoPane.setVisible(false);
    }
    
    /**
     * When the user clicks a quality or extension button, show its text in the
     * TextField
     * @param event action on a button
     */
    @FXML
    private void handleClick(MouseEvent event)
    {
        Button b1 = (Button)event.getSource();
        symbolTF.appendText(b1.getText());
    }
    
    /**
     * Not currently operable. This will initiate the actions that transpose
     * the current output for the instrument of choice.
     * @param instrument transpose output for this instrument
     */
    private void transpose(String instrument)
    {
        try
        {
            cu = new ChordUtil();
        } 
        catch (SQLException ex) {
            System.out.println("SQLException");
        }

    }
    
    /**
     * When the user clicks Submit, get the text in the textfield and call
     * the overloaded handleSubmit() to process it.
     */
    @FXML
    private void handleSubmit()
    {
        String entry = symbolTF.getText();
        handleSubmit(entry);
    }
    
    /**
     * Called indirectly, via the Submit button, or by transpose(). This class is
     * responsible for verifying that an entry is not empty and for calling the
     * ChordUtil methods that validate the entry characters, convert the entry
     * to a database-compatible format, and build Chord and Scale objects.
     * @param entry a chord symbol, before sanitization
     */
    private void handleSubmit(String entry)
    {
        errorMsg.setVisible(false);
        String origEntry = entry;   // For use in display
        MajorKey mKey = null;
        Chord chord1;
        
        // If not empty
        if(!entry.equals(""))
        {
            try
            {
                cu = new ChordUtil();
                // Normalize the input and verify that it contains only legal characters
                if(cu.isValidInput(entry, "^[a-z1-9\\/\\+\\-#°ø♭Δ ]{1,12}$", Normalizer.Form.NFD))
                {
                    String key = keyCombo.getValue();
                    
                    mKey = cu.buildKey(key);
                    
                    // Build the chord
                    entry = cu.formatSymbol(entry);
                    chord1 = cu.buildChord(entry, mKey);
                    
                    if(chord1 != null)
                    {
                        chord1.setDisplayName(key + " " + origEntry);
                        
                        // Put this Chord object in the Recent Chords ListView
                        rootCtrl.setRecentChord(chord1);
                        
                        // Build related Scale objects
                        cu.buildScales(chord1);
                        
                        // Display the newly built objects
                        buildOutput(chord1);
                    }
                }
                else
                {
                    // Highlight illegal char
                    alerter.setAlert
                        (Alert.AlertType.ERROR
                            , "Error"
                            , "Invalid characters"
                            , "Please make sure your entry only contains the characters indicated in red below the text field.");
                    errorMsg.setVisible(true);
                }
            }
            catch(SQLException ex)
            {
                alerter.setAlert
                    (Alert.AlertType.ERROR
                        , "Error"
                        , "Problem processing entry"
                        , "There was a problem processing your entry. "
                                + "The chord may not currently exist in the ChordCommand database. "
                                + "Contact the development team if problems persist.");
                System.out.println("Exception message: " + ex.getMessage());
                System.out.println("Exception cause: " + ex.getCause());
            }
        }
        else
        {
            alerter.setAlert
                (Alert.AlertType.ERROR
                        , "Error"
                        , "Empty text field"
                        , "Please make an entry in the text field before clicking Submit.");
        }
        symbolTF.requestFocus();
    } // End handleSubmit
    
    /**
     * Enable the transposition controls, clear the textfield, and call the
     * methods that will display output.
     * @param chord a Chord object
     */
    protected void buildOutput(Chord chord)
    {
        showChordDetails(chord);
        showScaleDetails(chord);
        instrCombo.setDisable(false);
        instrComboLbl.setDisable(false);
        symbolTF.clear();
    }
    
    /**
     * Display the pitches in the chord. Depending on user preferences, also
     * display a numeric representation and a piano graphic.
     * @param chord a Chord object to display
     */
    private void showChordDetails(Chord chord)
    {
        if(chord != null)
        {
            entryLbl.setText("You entered: " + chord.getDisplayName());
            chordTA.clear();
            chordTA.appendText("Pitches:\t\t");
            chordTA.appendText(chord.getStrPitches());
            
            // If showChordNum is true, display the string of chord numbers
            if(((ChordCommand)mainApp).getBooleanProp("showChordNum"))
            {
                chordTA.appendText("\nNumeric:\t\t");
                chordTA.appendText(chord.getStrNums());
            }
            
            // If showPiano property is true, prepare the piano graphic
            if(((ChordCommand)mainApp).getBooleanProp("showPiano"))
            {
                showPiano(chord);
            }
            else
            {
                pianoPane.setVisible(false);
            }
        }
    }// End showChordDetails()
    
    /**
     * Returns a list of 5 Chord objects
     * @return a list of 5 Chord objects, recently created
     */
    private ObservableList<Chord> getRecentChords()
    {
        return recentChords;
    }
    
    /**
     * Hide the TreeView in which Scales are displayed
     * @return true if scales were not hidden, false if hidden
     */
    private boolean hideScales()
    {
        boolean b = ((ChordCommand)mainApp).getBooleanProp("showAnyScale");
        if(!b)
        {
            scaleTree.setVisible(false);
            scalesLbl.setVisible(false);
        }
        return b;
    }
    
    /**
     * Create the tree representation of Scales that shows at least each Scale's
     * pitches and also, per user preferences, numeric and whole/half-step
     * representations.
     * @param chord a Chord object, whose Scale list is to be displayed
     */
    private void showScaleDetails(Chord chord)
    {
        // If hideScales returned true
        if(hideScales())
        {
            if(chord != null)
        {
            scaleTree.setVisible(true);
            scalesLbl.setVisible(true);
            
            // Create an empty root that will be hidden
            TreeItem<String> root = new TreeItem<>("");
            root.setExpanded(true);
            
            // Retrieve preferences
            boolean showScaleNum = ((ChordCommand)mainApp).getBooleanProp("showScaleNum");
            boolean showWHForm = ((ChordCommand)mainApp).getBooleanProp("showWH");
            
            
            /*Create root's children. Each child is a scale name, which in
            turn has a string of pitches and a string of numbers/accidentals
            as its children
            */
            for(Scale sc : chord.getScaleList())
            {
                TreeItem<String> name = new TreeItem<>(sc.getDisplayName());
                
                    addNode(true, sc.getStrPitches(), "Pitches:\t\t", name);
                    addNode(showScaleNum, sc.getStrNums(), "Numeric:\t\t", name);
                    addNode(showWHForm, sc.getStrWH(), "Whole/Half:\t", name);
                root.getChildren().add(name);
            }
            scaleTree.setRoot(root);
            scaleTree.setShowRoot(false);
        }
        }
    } // End showScaleDetails
    
    /**
     * Add a Scale representation--numeric, pitch-based, or whole-half step--
     * as parent's child
     * @param doShow user's preference regarding this representation
     * @param val the String to display
     * @param lbl a label like "Pitches" or "Numerical"
     * @param parent the parent TreeItem
     */
    private void addNode(boolean doShow, String val, String lbl, TreeItem<String> parent)
    {
        if(doShow)
        {
            TreeItem<String> child = new TreeItem<>(lbl + val);
            parent.getChildren().add(child);
        }
    }

    /**
     * Use the chord pitches to display red circles and pitch names to
     * indicate the keys that compose a given chord on the piano
     * @param chord1 a ChordObject
     */
    private void showPiano(Chord chord1)
    {
        // Clear existing markers
        pianoPane.getChildren().removeAll(markers);
        markers.clear();
        pianoPane.setVisible(true); // Set visible if not already
        
        PianoMap pKeys = new PianoMap();
        
        // Split the string of pitches on whitespace and process each separately
        String[] pitches = chord1.getStrPitches().split("-");
        int prevId = 0;
        int id;
        int octave = 0;
        
        // Convert each pitch to an ID that identifies a Rectangle
        for(String p : pitches)
        {
            id = pKeys.getKeyID(p);

            // Account for octave
            if((prevId - 12*octave) > id)
                octave++;
            
            id += 12*octave;
            prevId = id;

            // Get the Rectangle with this ID
            Rectangle rect = (Rectangle)((ChordCommand)mainApp).getScene().lookup("#" + id);

            // Create a label/circle object for display on this Rectangle
            KeyMarker markX = new KeyMarker(rect, p);
            markers.add(markX);
        }
        pianoPane.getChildren().addAll(markers);
    } // End showPiano()
} // End class ChordViewController
