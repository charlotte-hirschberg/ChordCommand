/**
 * GridPane that holds Menu icon, banner, Help icon, and chord input/output
 */

package chordcommand.view;

import chordcommand.ChordCommand;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class RootLayoutController {

    @FXML
    private GridPane frameControlsGP;
    @FXML
    private HBox centerPane;
    
    private ChordCommand main;


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
    
    /**
     * Called by main, so this class has a reference to it
     * @param main 
     */
    public void setMainApp(ChordCommand main)
    {
        this.main = main;
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