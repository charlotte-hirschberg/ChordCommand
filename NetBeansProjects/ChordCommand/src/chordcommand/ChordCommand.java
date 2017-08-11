package chordcommand;

//Imports
import chordcommand.util.AlertSetter;
import chordcommand.util.ChecksumUtil;
import chordcommand.util.DBUtil;
import chordcommand.util.PropertiesUtil;
import chordcommand.view.ChordViewController;
import chordcommand.view.HelpViewController;
import chordcommand.view.PrefDialogController;
import chordcommand.view.RootLayoutController;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/** 
 * @Course: SDEV 435 ~ Applied Software Practice
 * @Author Name: Charlotte Hirschberger
 * @Assignment ChordCommand
 * @Date: Jun 12, 2017
 * @Description: The ChordCommand class containing the project's main method. The
 * ChordCommand object is responsible for initializing the methods that load 
 * and save properties, load FXML files and link them to controllers, and populate
 * combo boxes.
 */
public class ChordCommand extends Application {
    private Stage primaryStage;
    private HBox centerPane;
    public DBUtil dbUtil;
    private ObservableList<String> instrComboData = FXCollections.observableArrayList();
    private ObservableList<String> keyComboData = FXCollections.observableArrayList();

    private final static String PREF_FILE = "src/chordcommand/userprefs.properties";
    private PropertiesUtil pu;
    private Properties prefs;
    private Scene scene;
    private RootLayoutController rootCtrl;
    private AlertSetter alerter;

    /**
     * Method inherited from the Application class
     * @param primaryStage the main screen
     */
    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        ChecksumUtil chku = new ChecksumUtil();
        alerter = new AlertSetter();       
        initDB();
        initRootLayout();
        setComboData();
        getPrefs();
        showChordView();

        
        // Show help view only if it is enabled to show up on startup
        if("1".equals(prefs.getProperty("showHelp")))
            showHelpView();
        
        
        // Save property changes on exit and close DataSource
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run()
            {
                try 
                {
                    pu.saveParamChanges(prefs, PREF_FILE);
                } 
                catch (IOException ex) 
                {
                    alerter.setAlert
                        (Alert.AlertType.ERROR
                                , "Error"
                                , "Error saving preferences"
                                , "Any changes to your preferences could not be saved.");

                }catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(ChordCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
                try
                {
                    dbUtil.closeDataSource();
                }
                catch(SQLException sqlEx)
                {
                    alerter.setAlert
                        (Alert.AlertType.ERROR
                                , "Error"
                                , "Problem closing connection"
                                , "The database connection could not be closed.");
                }
            }
        }); 
    }
    
    /**
     * Return the Scene object set in the main stage
     * @return a Scene object
     */
    public Scene getScene()
    {
        return scene;
    }
    
    /**
     * Get a user's preferences from PREF_FILE and create a Properties object 
     * with them
     */
    public void getPrefs()
    {
        pu = new PropertiesUtil();
        try 
        {
            prefs = pu.loadParams(PREF_FILE);
        } 
        catch (IOException | NoSuchAlgorithmException ex) {
            alerter.setAlert
                (Alert.AlertType.ERROR
                        , "Error"
                        , "Error loading preferences"
                        , "Your preferences could not be loaded. Default settings will be used.");
            setDefaultPrefs();
        }
    }
    
    /**
     * Set default preferences in the Properties file. Set all display options to
     * true and set showHelp to 1, causing it to display.
     */
    private void setDefaultPrefs()
    {
        String[] keys = {"showAnyScale","showWH","showScaleNum","showChordNum","showPiano"};
        prefs = new Properties();
        setSinglePref("showHelp", "1");
        setAllPrefs(keys, "true");
    }
    
    /**
     * Set every Property in keys to value
     * @param keys identifiers for properties
     */
    private void setAllPrefs(String[] keys, String value)
    {
        for(String key : keys)
        {
            prefs.setProperty(key, value);
        }
    }
    
    /**
     * Use the provided parameters to alter a property
     * @param key number that identifies this property
     * @param value the property's intended value
     */
    public void setSinglePref(String key, String value)
    {
        prefs.setProperty(key, value);
    }
    
    /**
     * Use the provided key to retrieve a property value
     * @param key number that identifies a property
     * @return the property's value as a String
     */
    public String getSinglePref(String key)
    {
        return prefs.getProperty(key);
    }
    
    /**
     * Use the provided key to retrieve a property with a true/false value
     * @param key number that identifies a property
     * @return the property's value converted to a Boolean
     */
    public boolean getBooleanProp(String key)
    {
        return Boolean.parseBoolean(getSinglePref(key));
    }
    
    /**
     * Return the primary stage's width
     * @return the primary stage's width
     */
    public double getStageWidth()
    {
        return primaryStage.getWidth();
    }
    
    /**
     * Retrieve data from the database and load it in lists for use in ComboBoxes
     */
    private void setComboData()
    {
        // Get Instrument names to fill ComboBox with "Transpose to" label
        String query1 = "SELECT Instrument FROM TransposingKeyLU";
        
        // Get key names, like "Bb" to fill the ComboBox with label "Key"
        String query2 = "SELECT MajorKeyName FROM MajorKeyLU ";
        
        try {
            instrComboData = dbUtil.toList(query1, "Instrument");
            keyComboData = dbUtil.toList(query2, "MajorKeyName");
        } 
        catch (SQLException ex) {
            alerter.setAlert
                (Alert.AlertType.ERROR
                        , "Error"
                        , "Error loading elements"
                        , "ChordCommand's dropdown menus could not be filled. The program will now exit.");
            System.exit(1);
        }
    }
    
    /**
     * Returns the musical keys as an observable list of Strings
     * @return the list of musical key Strings
     */
    public ObservableList<String> getKeyComboData() 
    {
        return keyComboData;
    }
        
    /**
     * Returns the transposing instrument names as an observable list of Strings
     * @return the list of instrument Strings
     */
    public ObservableList<String> getInstrComboData() 
    {
        return instrComboData;
    }
    
    /**
     * Initializes a set of Properties with database credentials persisted in a
     * file
     */
    private void initDB()
    {
        try
	{
            dbUtil = new DBUtil();
	}
        catch(IOException | NoSuchAlgorithmException e)
        {
            alerter.setAlert
                (Alert.AlertType.ERROR
                        , "Error"
                        , "Database connection error"
                        , "ChordCommand is unable to connect to the database. The program will now exit.");
            System.exit(1);
        }
    }

    /**
     * Initializes the root layout, a GridPane that holds icons and chord 
     * input/output
     */
    private void initRootLayout() 
    {
        // Load the FXML and link it to a controller
        RegionControl rc = new RegionControl("view/RootLayout.fxml", this);
        
        // Set the root layout pane in a scene
        primaryStage = prepareStage("Chord Command", rc.getRegion(), null);
        primaryStage.setMaximized(true);
        scene = primaryStage.getScene();
            
        rootCtrl = (RootLayoutController)rc.getController();
        centerPane = rootCtrl.getCenterPane();
        rootCtrl.setGridWidth(primaryStage.widthProperty());           
    }
    
    /**
     * Initialize the help view, a pane that displays a screenshot and tips for
     * new users
     */
    public void showHelpView() 
    {
        // Load the FXML file and link it to a controller
        RegionControl rc = new RegionControl("view/HelpView.fxml", this);
        
        // Create the dialog Stage
        Stage helpStage = prepareStage("Quick Start Guide", rc.getRegion(), primaryStage);
           
        HelpViewController ctrller = (HelpViewController)rc.getController();  
        
        // Give the controller a reference to the newly created stage
        ctrller.setHelpStage(helpStage);
    }
        
    /**
     * Initialize the chord view, a pane that holds the input controls, output
     * panes, and the piano graphic. The chord view resides in the root layout.
     */
    private void showChordView() 
    {
        // Load the FXML file and link it to a controller
        RegionControl rc = new RegionControl("view/ChordView.fxml", this);
        
        // Insert the new pane inside the root layout
        centerPane.getChildren().add(rc.getRegion());
        
        ChordViewController ctrller = (ChordViewController)rc.getController();        
        ctrller.setCombos();
        ctrller.initOutputArea();
        
        // Give RootLayoutController a link to ChordViewController and vice versa
        ctrller.setRootCtrl(rootCtrl);
        rootCtrl.setChordCtrl(ctrller);
    }
    
    /**
     * Initialize the preferences dialog, a pane that holds check boxes that
     * control the chord/scale output. Values selected with the check boxes are
     * later persisted in a .properties file.
     */
    public void showPrefView()
    {
        // Load the FXML file and link it to a controller
        RegionControl rc = new RegionControl("view/PrefDialog.fxml", this);
        
        // Create a stage for the new pane
        Stage prefStage = prepareStage("Set Preferences", rc.getRegion(), primaryStage);
        
        PrefDialogController ctrller = (PrefDialogController)rc.getController();
        
        // Give the controller a reference to the new stage
        ctrller.setPrefStage(prefStage);
        
        // Use the properties file to set the checkboxes as selected/deselected
        ctrller.initCBValues();
    }
    
    /**
     * Prepare a new stage with the provided name and layout pane and set
     * Stage parent as its owner, as necessary.
     * @param name  the stage title
     * @param paneX a layout pane
     * @param parent the stage that owns this one, optional
     * @return the new stage
     */
    private Stage prepareStage(String name, Region paneX, Stage parent)
    {
        Stage stage1 = new Stage();
        if(parent != null)
            stage1.initOwner(parent);
	stage1.setTitle(name);
	Scene scene1 = new Scene(paneX);
	stage1.setScene(scene1);
	stage1.show();
           
        return stage1;
    }
    
    /**
     * Close the primary stage
     */
    public void causeExit()
    {
        primaryStage.close();
    }

    /**
     * The main method
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
} //End Class ChordCommand