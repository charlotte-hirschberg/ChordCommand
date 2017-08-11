package chordcommand.view;

import chordcommand.ChordCommand;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/** 
 * @Course: SDEV 435 ~ Applied Software Practice
 * @Author Name: Charlotte Hirschberger
 * @Assignment ChordCommand
 * @Date: Jun 12, 2017
 * @Description: The Controller object that is responsible for receiving and
 * processing input from controls in PrefDialog.fxml. Its primary duties are
 * setting and processing each checkbox's SelectedProperty, closing the stage,
 * and handling button clicks.
 */

public class PrefDialogController extends Controller
{
    private Stage prefStage;
    
    // CheckBoxes that control properties
    @FXML
    private CheckBox cbChordNum;
    @FXML
    private CheckBox cbWH;
    @FXML
    private CheckBox cbAnyScale;
    @FXML
    private CheckBox cbScaleNum;
    @FXML
    private CheckBox cbPiano;
    
    private CheckBox[] checks;
    
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded. It loads the check boxes into an
     * array for easier processing later and provides a listener for the
     * "Show Scales" check box. The listener disables the check boxes beneath
     * "Show Scales" when it is deselected.
     */
    @FXML
    private void initialize() 
    {
        checks = new CheckBox[5];
        checks[0] = cbChordNum;
        checks[1] = cbPiano;
        checks[2] = cbAnyScale;
        checks[3] = cbScaleNum;
        checks[4] = cbWH;
        
        cbAnyScale.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> 
                {
                        cbScaleNum.setDisable(!new_val);
                        cbWH.setDisable(!new_val);
                });
    }
   
    /**
     * Initialize the check boxes with values from the preferences file
     * that were retrieved during startup.
     */
   public void initCBValues()
   {
        for(CheckBox cb : checks)
        {
            String name = cb.getId();
            boolean isSelected = Boolean.parseBoolean(((ChordCommand)mainApp).getSinglePref(name));
            cb.setSelected(isSelected);
        }
   }
   
     /**
     * Sets the stage of this dialog.
     * @param prefStage the stage for this dialog
     */
    public void setPrefStage(Stage prefStage) {
        this.prefStage = prefStage;
        prefStage.setResizable(false);
    }
    
    /**
     * Handle event on OK button by processing check box selections
     */
    @FXML
    private void handleOK()
    {
        /*get an ID and selectedProperty() and set the
        corresponding Properties value*/
        for(int i = 0; i < 5; i++)
            ((ChordCommand)mainApp).setSinglePref(checks[i].getId(), String.valueOf(checks[i].isSelected()));
        
        handleCancel();
    }
    
    /**
     * Close the Preferences dialog. If not called after handleOK(), this
     * causes the dialog to close without changes being saved.
     */
    @FXML
    public void handleCancel()
    {
        prefStage.close();
    }
} // End PrefDialogController