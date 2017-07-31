/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chordcommand.view;

import chordcommand.ChordCommand;
import chordcommand.ChordUtil;
import model.Chord;
import model.MajorKey;
import chordcommand.PianoMap;
import model.Scale;
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

/**
 *
 * @author Charlotte
 */
public class ChordViewController {

    @FXML
    private TextField keyTF;
    @FXML
    private TextField symbolTF;
    @FXML
    private ComboBox<String> instrCombo;
    @FXML
    private ComboBox<String> keyCombo;
    @FXML
    private Label entryLbl;
    @FXML
    private Label scalesLbl;
    @FXML 
    private Label instrComboLbl;
    @FXML
    private TextArea chordTA;
    @FXML
    private TreeView scaleTree;
    @FXML
    private Pane pianoPane;
    
    private ChordCommand main;
    private ChordUtil cu;
    private ArrayList<KeyMarker> markers;
    private ObservableList<Chord> recentChords;
    
    private RootLayoutController rootCtrl;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() 
    {
        recentChords = FXCollections.observableArrayList();
        instrCombo.setDisable(true);
        instrComboLbl.setDisable(true);
        markers = new ArrayList();
        keyTF.setEditable(false);
        
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

    public void setCombos()
    {
         setCombo(instrCombo, "Flute", main.getInstrComboData());
         setCombo(keyCombo, "C", main.getKeyComboData());
    }
    
    private void setCombo(ComboBox combo, String title, ObservableList<String> data)
    {
        combo.setItems(data);
        combo.setValue(title);
    }
    
        /**
     * Called by main, so this class has a reference to it
     * @param main 
     */
    public void setMainApp(ChordCommand main)
    {
        this.main = main;
    }
    
    public void setRootCtrl(RootLayoutController ctrller)
    {
        rootCtrl = ctrller;
    }
    
    public void initOutputArea()
    {
        // If showAnyScale = false, hide scaleTree and its label
        hideScales();
        
        // If pianoPane = false, hide its label
        if(!main.getBooleanProp("showPiano"))
            pianoPane.setVisible(false);
    }
    
    @FXML
    private void handleClick(MouseEvent event)
    {
        Button b1 = (Button)event.getSource();
        symbolTF.appendText(b1.getText());
    }
    
    private void transpose(String instrument)
    {
        try
        {
            cu = new ChordUtil();
            // Get this instrument's key
            // Get the most recent Chord's MajorKey
        } 
        catch (SQLException ex) {
            System.out.println("SQLException");
        }

    }
    
    @FXML
    private void handleSubmit()
    {
        String entry = symbolTF.getText();
        handleSubmit(entry);
    }
    
    public void handleSubmit(String entry)
    {
        String origEntry = entry;
        MajorKey mKey = null;
        Chord chord1;
        
        if(!entry.equals(""))
        {
            try
            {
                cu = new ChordUtil();
                if(cu.isValidInput(entry, "^[majsuinbd1-9\\+\\-#°ø♭Δ]{1,12}$", Normalizer.Form.NFD))
                {
                    String key = keyCombo.getValue();
                    
                    mKey = cu.buildKey(key);
                    
                    // Build the chord
                    entry = cu.formatSymbol(entry);
                    chord1 = cu.buildChord(entry, mKey);
                    
                    if(chord1 != null)
                    {
                        chord1.setDisplayName(key + " " + origEntry);
                        rootCtrl.setRecentChord(chord1);
                        cu.buildScales(chord1);
                        
                        buildOutput(chord1);
                    }
                }
                else
                {
                    // Highlight illegal char
                    System.out.println("Illegal char");
                }
                
            }
            catch(SQLException ex)
            {
                System.out.println("SQLException");
            }
        }
        else
        {
            // Highlight missing entry
        }
    }
    
    public void buildOutput(Chord chord)
    {
        showChordDetails(chord);
        showScaleDetails(chord);
        instrCombo.setDisable(false);
        instrComboLbl.setDisable(false);
        symbolTF.clear();
    }
    
    private void showChordDetails(Chord chord)
    {
        if(chord != null)
        {
            entryLbl.setText("You entered: " + chord.getDisplayName());
            chordTA.clear();
            chordTA.appendText("\nPitches:\t\t");
            chordTA.appendText(chord.getStrPitches());
            
            if(main.getBooleanProp("showChordNum"))
            {
                chordTA.appendText("\nNumeric:\t\t");
                chordTA.appendText(chord.getStrNums());
            }
            if(main.getBooleanProp("showPiano"))
            {
                showPiano(chord);
            }
            else
            {
                pianoPane.setVisible(false);
            }
        }
    }
    
    private ObservableList<Chord> getRecentChords()
    {
        return recentChords;
    }
    
    private boolean hideScales()
    {
        boolean b = main.getBooleanProp("showAnyScale");
        if(!b)
        {
            scaleTree.setVisible(false);
            scalesLbl.setVisible(false);
        }
        return b;
    }
    
    private void showScaleDetails(Chord chord)
    {
        if(hideScales())
        {
            if(chord != null)
        {
            scaleTree.setVisible(true);
            scalesLbl.setVisible(true);
            // Create an empty root that will be hidden
            TreeItem<String> root = new TreeItem<>("");
            root.setExpanded(true);
            
            boolean showScaleNum = main.getBooleanProp("showScaleNum");
            boolean showWHForm = main.getBooleanProp("showWHForm");
            
            
            /*Create root's children. Each child is a scale name, which in
            turn has a string of pitches and a string of numbers/accidentals
            as its children
            */
            for(Scale sc : chord.getScaleList())
            {
                TreeItem<String> name = new TreeItem<>(sc.getDisplayName());
                
                    addNode(true, sc.getStrPitches(), "Pitches:\t\t", name);
                    addNode(showScaleNum, sc.getStrNums(), "Numerical:\t\t", name);
                    addNode(showWHForm, sc.getStrWH(), "Whole/Half:\t", name);
                root.getChildren().add(name);
            }
            scaleTree.setRoot(root);
            scaleTree.setShowRoot(false);
        }
        }
    }
    
    public void addNode(boolean doShow, String val, String lbl, TreeItem<String> parent)
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
     * @param chord1 
     */
    private void showPiano(Chord chord1)
    {
        pianoPane.getChildren().removeAll(markers);
        markers.clear();
        pianoPane.setVisible(true); // Set visible if not already
        
        PianoMap pKeys = new PianoMap();
        String[] pitches = chord1.getStrPitches().split("\\s+");
        int prevId = 0;
        int id;
        int octave = 0;
        
        for(String p : pitches)
        {
            id = pKeys.getKeyID(p);

            // Account for octave
            if((prevId - 12*octave) > id)
                octave++;
            
            id += 12*octave;
            prevId = id;

            // Get the Rectangle with this ID
            Rectangle rect = (Rectangle)main.getScene().lookup("#" + id);

            KeyMarker markX = new KeyMarker(rect, p);
            markers.add(markX);
        }
        pianoPane.getChildren().addAll(markers);
    }
}
