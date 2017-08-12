package chordcommand.view;

import chordcommand.ChordCommand;
import chordcommand.model.Chord;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/** 
 * Description: The RootLayoutController class is responsible for actions on
 * the controls that frame the chord output, including the menu, banner, help icon,
 * and Recent Chords. The controls are positioned in a GridPane.
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class RootLayoutController extends Controller {

    @FXML
    private GridPane frameControlsGP;
    @FXML
    private HBox centerPane;
    @FXML
    private ListView<Chord> recentChords;
    private ObservableList<Chord> chordData = FXCollections.observableArrayList();
    
    private ChordViewController chordCtrl;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. Its primary responsibilities are:
     * populating the Recent Chords ListView; updating the ListView whenever a
     * Chord object is added to chordData, via the input area; and changing the
     * output to reflect clicks on Recent Chord items.
     */
    @FXML
    private void initialize() 
    {
        recentChords.setItems(chordData);    
        recentChords.setCellFactory(new Callback<ListView<Chord>, ListCell<Chord>>() 
        {
            @Override 
            public ListCell<Chord> call(ListView<Chord> list) 
            {
                return new ListCell<Chord>()
                {
                    @Override
                    protected void updateItem(Chord item, boolean empty) 
                    {
                        super.updateItem(item, empty);

                        if (item == null) 
                            setText("");
                        else 
                            setText(item.getDisplayName());
                    }
                };
            }
        });
        
        recentChords.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            chordCtrl.buildOutput(newValue);
        });
    }
    
    /**
     * Give this a reference to the ChordViewController, so clicking the
     * Recent Chords list produces a change in output.
     * @param ctrller a ChordViewController object
     */
    public void setChordCtrl(ChordViewController ctrller)
    {
        chordCtrl = ctrller;
    }
    
    /**
     * Add the latest Chord to the ObservableList used to populate Recent Chords.
     * If the ListView already contains 5 entries, this removes the one with the
     * lowest lexographical value. Eventually, chordData should implement a
     * Comparator that maintains a timestamp-based sort, instead of an
     * alphabetical one.
     * @param chord a Chord object
     */
    public void setRecentChord(Chord chord)
    {   
        if(chordData.size() >= 5)
             chordData.remove(0);     

        chordData.add(chord);
    }
    
    /**Set the GridPane's width so it fills the primary Stage
     * @param w the primary stage's width
     */
    public void setGridWidth(ReadOnlyDoubleProperty w)
    {
        frameControlsGP.prefWidthProperty().bind(w.subtract(40));
    }
    
    /**
     * Return the layout at 1,1, so ChordView can be placed in it
     * @return HBox the pane that will hold chord output
     */
    public HBox getCenterPane()
    {
        return centerPane;
    }
    
    /**
     * Handle clicks on Help icon in top-right corner
     */
    public void handleHelp()
    {
        ((ChordCommand)mainApp).showHelpView();
    }
    
    /**
     * Load the Preferences stage, on menu click
     */
    public void handlePrefItem()
    {
        ((ChordCommand)mainApp).showPrefView();
    }
    
    /**
     * Handle About item in the menu
     */
    public void handleAboutItem()
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About Chord Command");
        alert.setHeaderText("Chord Command");
        alert.setContentText("Version 1.0.0\nCharlotte Hirschberger, 2017\nAll rights reserved.");
        alert.showAndWait();
    }
    
    /**
     * Handle Close option in the menu
     */
    public void handleCloseItem()
    {
        ((ChordCommand)mainApp).causeExit();
    } 
} // End class RootLayoutController
