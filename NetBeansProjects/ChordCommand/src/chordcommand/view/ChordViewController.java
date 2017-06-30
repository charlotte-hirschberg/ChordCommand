/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chordcommand.view;

import chordcommand.ChordCommand;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Charlotte
 */
public class ChordViewController {

    @FXML
    private ToggleGroup pitchRBs;
    @FXML
    private TextField symbolTF;
    @FXML
    private ComboBox<String> pitchCombo;
    private ObservableList<String> pitchComboData = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> instrCombo;
    private ObservableList<String> instrComboData = FXCollections.observableArrayList();
    
    private ChordCommand main;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() 
    {
        pitchRBs.selectedToggleProperty().addListener(
            (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> 
            {
                if (pitchRBs.getSelectedToggle() != null) 
                {
                    RadioButton pitch = (RadioButton)pitchRBs.getSelectedToggle(); // Cast object to radio button
                    String chosenPitch = pitch.getText();
                    String currText = symbolTF.getText();
                    
                    if(!"".equals(currText))
                        symbolTF.setText(chosenPitch + currText.substring(1));
                    else
                        symbolTF.setText(chosenPitch);
                }    
            }
        );
    }

    public void setCombos(ObservableList<String> data1, ObservableList<String> data2)
    {
        instrCombo.setItems(data1);
        pitchCombo.setItems(data2);
        
        instrCombo.setValue("Flute");
        pitchCombo.setValue("C");
    }
    
        /**
     * Called by main, so this class has a reference to it
     * @param main 
     */
    public void setMainApp(ChordCommand main)
    {
        this.main = main;
    }
    
    @FXML
    private void handleClick(MouseEvent event)
    {
        Button b1 = (Button)event.getSource();
        symbolTF.appendText(b1.getText());
    }
    
    
}
