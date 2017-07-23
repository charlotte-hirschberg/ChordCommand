/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chordcommand.view;

import chordcommand.ChordCommand;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/**
 *
 * @author Charlotte
 */
public class PrefDialogController 
{
    private Stage prefStage;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnOk;
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
    
    private ChordCommand main;
    
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
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
    
    public void setMainApp(ChordCommand main)
    {
        this.main = main;
    }
    
   public void initCBValues()
   {
        // Retrieve their initial values from Preferences file
        for(CheckBox cb : checks)
        {
            String name = cb.getId();
            boolean isSelected = Boolean.parseBoolean(main.getSinglePref(name));
            cb.setSelected(isSelected);
        }
   }
   
     /**
     * Sets the stage of this dialog.
     * 
     * @param prefStage
     */
    public void setHelpStage(Stage prefStage) {
        this.prefStage = prefStage;
        prefStage.setResizable(false);
    }
    
    /**
     * Handle event on OK button by processing checkbox selections
     */
    public void handleOK()
    {
        /*First 3 options require no special handling.
        Just get an ID and selectedProperty() and set the
        corresponding Properties value*/
        for(int i = 0; i < 3; i++)
            main.setSinglePref(checks[i].getId(), String.valueOf(checks[i].isSelected()));
        
        /*Options 4 & 5 are dependent on Show Scales value*/
        
        /*'Show Scales' is selected, so change its children to reflect their
        current state, whatever it */
        if(checks[2].isSelected() == true)
        {
            main.setSinglePref(checks[3].getId(), String.valueOf(checks[3].isSelected()));
            main.setSinglePref(checks[4].getId(), String.valueOf(checks[4].isSelected()));
        }
        
        //'Show Scales' was deselected, so deselect its children
        else
        {
            main.setSinglePref(checks[3].getId(), "false");
            main.setSinglePref(checks[4].getId(), "false");
        }
        handleCancel();
    }
    
    /**
     * Closes the Preferences dialog without recording changes
     */
    public void handleCancel()
    {
        prefStage.close();
    }
   
}
