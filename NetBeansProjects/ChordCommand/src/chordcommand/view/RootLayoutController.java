package chordcommand.view;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Dialog to edit details of a person.
 * 
 * @author Marco Jakob
 */
public class RootLayoutController {

    @FXML
    private GridPane frameControlsGP;
    @FXML
    private HBox centerPane;


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
    
    public GridPane getFrameControlsGP()
    {
        return frameControlsGP;
    }
    
    public HBox getCenterPane()
    {
        return centerPane;
    }
}