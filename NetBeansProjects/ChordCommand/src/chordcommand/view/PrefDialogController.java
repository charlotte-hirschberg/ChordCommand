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

/**
 *
 * @author Charlotte
 */
public class PrefDialogController 
{
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
    
    public void handleOK()
    {
        // Get the value 
    }
   
}
