package chordcommand.view;

import chordcommand.ChordCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/** 
 * Description: The HelpViewController class interacts with the HelpView.fxml
 * file, obtaining input from a Button and CheckBox and displaying the Stage
 * when the question-mark icon is clicked. This relies on Controller's empty
 * initialize method.
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class HelpViewController extends Controller
{

    @FXML
    private CheckBox noShowCB; // show help on next startup?
    @FXML
    private Button gotIt;

    private Stage helpStage;

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
     * Called when the user clicks ok. Closes the stage and sets the showHelp
     * preference to false if the checkbox is selected.
     */
    @FXML
    private void handleOk() 
    {
        if(noShowCB.isSelected())
            ((ChordCommand)mainApp).setSinglePref("showHelp", "0");
        helpStage.close();
    }
} // End class HelpViewController