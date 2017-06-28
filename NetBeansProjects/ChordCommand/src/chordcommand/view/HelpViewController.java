package chordcommand.view;

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

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

        /**
     * Sets the stage of this dialog.
     * 
     * @param helpStage
     */
    public void setHelpStage(Stage helpStage) {
        this.helpStage = helpStage;
    }
    
    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
    {
        if(noShowCB.isSelected())
        {
            // Code to update XML will go here
            System.out.println("Calling savePrefsToFile()");
        }
            helpStage.close();
        }
    }
}