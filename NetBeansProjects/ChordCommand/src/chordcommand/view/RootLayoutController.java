/**
 * GridPane that holds Menu icon, banner, Help icon, and chord input/output
 */

package chordcommand.view;

import model.Chord;
import chordcommand.ChordCommand;
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

public class RootLayoutController {

    @FXML
    private GridPane frameControlsGP;
    @FXML
    private HBox centerPane;
    @FXML
    private ListView<Chord> recentChords;
    private ObservableList<Chord> chordData = FXCollections.observableArrayList();
    
    private ChordCommand main;
    private ChordViewController chordCtrl;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
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
     * Called by main, so this class has a reference to it
     * @param main 
     */
    public void setMainApp(ChordCommand main)
    {
        this.main = main;
    }
    
    public void setChordCtrl(ChordViewController ctrller)
    {
        chordCtrl = ctrller;
    }
    
    public void setRecentChord(Chord chord)
    {
        if(chordData.size() > 5)
            chordData.remove(0);
        chordData.add(chord);
    }
    
    public void setGridWidth(ReadOnlyDoubleProperty w)
    {
        frameControlsGP.prefWidthProperty().bind(w.subtract(40));
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
    
    /**
     * Handle Preferences option in the menu
     */
    public void handlePrefItem()
    {
        main.showPrefView();
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
        main.causeExit();
    }
    
}