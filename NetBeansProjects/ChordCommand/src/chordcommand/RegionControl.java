package chordcommand;

import chordcommand.util.AlertSetter;
import chordcommand.view.Controller;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

/** 
 * Description: This class encapsulates Controller and Region members with
 * accessors and methods that load an FXML file and link it to the Controller
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class RegionControl {
    private Controller ctrller;     // Custom class
    private Region paneX;           // a Pane or Control object
    private static AlertSetter alerter = new AlertSetter();
    
    /**
     * Constructor that loads FXML from the provided path and links the FXML
     * controller. The controller receives a reference to the main application.
     * @param path file path
     * @param app an Application object containing the main method
     */
    public RegionControl(String path, Application app)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource(path));
            paneX = loader.load();
            
            ctrller = loader.getController();
            ctrller.setMainApp(app);
        }
        catch (IOException e)
        {
            alerter.setAlert
                (Alert.AlertType.ERROR
                        , "Error"
                        , "Error loading elements"
                        , "One or more graphic elements could not be displayed. The program will now exit.");
            System.exit(1);
        }
    }
    
    /**
     * Return this object's Controller
     * @return this object's Controller
     */
    public Controller getController()
    {
        return ctrller;
    }
    
    /**
     * Returns this object's Pane or Control object
     * @return this object's Region
     */
    public Region getRegion()
    {
        return paneX;
    }
} // End class RegionControl
