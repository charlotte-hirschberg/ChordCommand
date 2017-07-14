/**
 * GridPane that holds Menu icon, banner, Help icon, and chord input/output
 */

package chordcommand.view;

import chordcommand.Chord;
import chordcommand.ChordCommand;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class RootLayoutController {

    @FXML
    private GridPane frameControlsGP;
    @FXML
    private HBox centerPane;
    @FXML
    private HBox holdHelp;
    @FXML
    private ListView<Chord> recentChords;
    private ObservableList<Chord> chordList = FXCollections.observableArrayList();
    
    private ChordCommand main;


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() 
    {
    }
    
    /**
     * Called by main, so this class has a reference to it
     * @param main 
     */
    public void setMainApp(ChordCommand main)
    {
        this.main = main;
    }
    
    public void setGridWidth(ReadOnlyDoubleProperty w)
    {
        frameControlsGP.prefWidthProperty().bind(w);
    }
    
    /**
     * Return the layout at 1,1, so ChordView can be placed in it
     * @return HBox
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
        main.showHelpView();
    }
    
    
}