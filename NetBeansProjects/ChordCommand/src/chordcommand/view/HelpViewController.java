package chordcommand.view;

import chordcommand.ChordCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;


public class HelpViewController {

    @FXML
    private CheckBox noShowCB;
    @FXML
    private Button gotIt;

    private Stage helpStage;
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
     * Sets the stage of this dialog.
     * 
     * @param helpStage
     */
    public void setHelpStage(Stage helpStage) {
        this.helpStage = helpStage;
        helpStage.setResizable(false);
    }
    
    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() 
    {
        if(noShowCB.isSelected())
            main.setSinglePref("showHelp", "0");
        helpStage.close();
    }
}