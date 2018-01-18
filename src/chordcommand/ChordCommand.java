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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/** 
 * Description: The ChordCommand class containing the project's main method. The
 * ChordCommand object is responsible for initializing the methods that load 
 * and save properties, load FXML files and link them to controllers, and populate
 * combo boxes.
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class ChordCommand extends Application {
    // Layout components
    private Stage primaryStage;
    private HBox centerPane;
    private Scene scene;
    private RootLayoutController rootCtrl;
    
    
    // Tools for use when loading and storing data from persistence technologies
    public DBUtil dbUtil;
    private ObservableList<String> instrComboData = FXCollections.observableArrayList();
    private ObservableList<String> keyComboData = FXCollections.observableArrayList();
    private final static String PREF_FILE = "userprefs.properties";
    private final static String DB_FILE = "dbprops.properties";
    private PropertiesUtil pu;
    private Properties prefs;
    private Properties dbParam;
    private static ChecksumUtil chkUtil;
    
    // Tools & flags for use on exit
    private AlertSetter alerter;   
    private boolean wasFileExc = false;
    private boolean wasDBMismatch = false;
    private boolean wasPrefsIO = false;
    private boolean wasAlgoExc = false;
    private static int ctExitExcep;
    private static String exitMsg;

    /**
     * Method inherited from the Application class
     * @param primaryStage the main screen
     */
    @Override
    public void start(Stage primaryStage) 
    {
        this.primaryStage = primaryStage;
        alerter = new AlertSetter();  
        chkUtil = new ChecksumUtil(); 
        pu = new PropertiesUtil();
        initDB();
        initRootLayout();
        setComboData();
        getPrefs();
        showChordView();

        
        // Show help view only if it is enabled to show up on startup
        if("1".equals(prefs.getProperty("showHelp")))
            showHelpView();
    }
    
 /**Overrides Application class's stop() method, in order to save property
     * changes, close data sources, and reconcile problems with checksum files.
     */
    
    @Override
    public void stop(){
        runCloseRoutine();
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
     * Uses a checksum to verify that the preferences file hasn't been tampered
     * with since ChordCommand last exited. If an exception occurs or the
     * checksums don't match, default preferences are applied. Otherwise, the
     * user's preferences are retrieved from PREF_FILE and a Properties object is
     * created with them.
     */
    public void getPrefs()
    {
        boolean isMatch = false;
        String errMsg = "";
        
        try
	{
            // Does the file match the checksum computed at last exit?
            isMatch = chkUtil.compareChecksums(PREF_FILE);
	}
        catch(NoSuchAlgorithmException excAlgo)
        {
            wasAlgoExc = true;
            errMsg = "Your preferences could not be loaded. Default values will be used instead."
                                + " Try updating Java to avoid this error in the future.";
        }
        catch(IOException excIO)
        {
            wasPrefsIO = true;
            errMsg = "Your preferences could not be loaded. ChordCommand will try to restore your "
                    + "preferences on exit, but default values will be used for this session.";
        } // End checksum comparison
        
        // Checksums couldn't be compared or compareChecksums returned false
        if(!isMatch)
        {
            setDefaultPrefs();
            // compareChecksums returned false
            if("".equals(errMsg))
                errMsg = "One or more files appear to be corrupted, so your preferences could not be loaded. "
                    + "Default values will be used instead.";
            
            alerter.setAlert
                (Alert.AlertType.ERROR
                        , "Error"
                        , "Error loading preferences"
                        , errMsg);
        }
        
        // Checksums matched; safe to try to load user preferences
        else
        {
            try
            {
                prefs = pu.loadParams(PREF_FILE);
            } 
            
            // Preferences could not be loaded
            catch (IOException ex) {
                wasPrefsIO = true;
                setDefaultPrefs();
                alerter.setAlert
                (Alert.AlertType.ERROR
                    , "Error"
                    , "Error loading preferences"
                    , "Your preferences could not be loaded. ChordCommand will try to restore your "
                    + "preferences on exit, but default values will be used for this session.");
            }
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
     * Uses a checksum to verify that the database-parameters file hasn't been 
     * tampered with. If an exception occurs or the checksums don't match, a warning
     * is provided, and the user can choose to proceed or quit. From there, if the
     * properties fail to load successfully, ChordCommand will exit. When a file
     * exception occurs and the user chooses to risk proceeding, ChordCommand
     * will attempt to make a new .chk file on shutdown.
    */
    private void initDB()
    {
        boolean isMatch = false;
        try
	{
            isMatch = chkUtil.compareChecksums(DB_FILE);
            
            // Checksum executed successfully
            // compareChecksums returned false
            if(!isMatch)
            {

                Alert chkAlert = alerter.getAlert
                    (Alert.AlertType.CONFIRMATION
                        , "Warning"
                        , "Database connection error"
                        , "One or more database files appear to be corrupted. We recommend re-installing ChordCommand. "
                                + "Proceeding may put your computer at risk. "
                                + "Would you like to proceed? (Choosing No will close ChordCommand)");
            
                alerter.setYesNo(chkAlert);
                wasDBMismatch = true;
            }
	}
        // Exceptions while calculating checksums
        catch(NoSuchAlgorithmException excAlgo)
        {
            wasAlgoExc = true;
            Alert algoAlert = alerter.getAlert
                (Alert.AlertType.CONFIRMATION
                        , "Warning"
                        , "Database connection error"
                        , "ChordCommand was unable to verify the authenticity of certain database files. "
                                + "Updating Java may correct the problem, but proceeding may put your computer at risk. Would you like to proceed? "
                                + "(Choosing No will close ChordCommand)");
            
            alerter.setYesNo(algoAlert);
        }
        catch(FileNotFoundException excFile)
        {
            Alert fileAlert = alerter.getAlert
                (Alert.AlertType.CONFIRMATION
                        , "Warning"
                        , "Database connection error"
                        , "ChordCommand was unable to verify the authenticity of certain database files. "
                                + "Proceeding may put your computer at risk, but ChordCommand may be able to fix the problem. "
                                + "Would you like to proceed? (Choosing No will close ChordCommand)");
            
            alerter.setYesNo(fileAlert);
            wasFileExc = true; // flag this, so ChordCommand knows to try to fix the issue on exit
            
        }
        catch(IOException excIO)
        {
            Alert ioAlert = alerter.getAlert
                (Alert.AlertType.CONFIRMATION
                        , "Warning"
                        , "Database connection error"
                        , "ChordCommand was unable to verify the authenticity of certain database files. "
                                + "Restarting ChordCommand may fix the problem, but proceeding may put your computer at risk. "
                                + "Would you like to proceed? (Choosing No will close ChordCommand)");
            
            alerter.setYesNo(ioAlert);
        } // End checksum comparison
        

        // If you get here without system exiting, user chose Yes or checksum yielded match
        // Attempt to load parameters
        try
        {
            dbParam = pu.loadParams(DB_FILE);
        }
        catch(IOException ex)
        {
            alerter.setAlert
            (Alert.AlertType.ERROR
                , "Error"
                , "Database connection error"
                , "A database connection could not be established. If the problem persists, "
                        + "consider re-installing ChordCommand. The system will now exit.");
            System.exit(1);
        }
        
        // If you get here, checking and loading the files succeeded
        dbUtil = new DBUtil(dbParam);
    } // End initDB()

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
     * Runs on exit and uses utility classes to record properties, create 
     * checksum files, and close database connections if possible. Tries to
     * correct problems encountered while computing checksums on startup.
     * Corrective actions include making a new .properties and .chk file with
     * default values.
     */
    private void runCloseRoutine()
    {
        exitMsg = "";
        ctExitExcep = 0;
                
        /*If a NoSuchAlgorithmException occurred during execution, don't
        attempt to write any hashes or rewrite preferences, as the
        problem will recur until Java version is changed.*/
        if(!wasAlgoExc)
        {
            /*If the user accepted the risk after a file exception or
            checksum mismatch during initDB() and properties were successfully
            retrieved and used, try to fix the .chk file*/
            if(dbUtil.getSuccess() && (wasFileExc || wasDBMismatch))
            {
                try
                {
                    chkUtil.createCheckFile(DB_FILE);
                    
                    /*Take care of storing preferences*/
                    runChecksumExitRoutine();
                } 
                catch (NoSuchAlgorithmException algoEx)
                {
                    /*It is unlikely that the program will ever reach this block.
                    If it does, note the error and skip to closing database, as
                    further checksum actions will likely fail*/
                    exitMsg += "Error " + ++ctExitExcep + ": Failed to fix a file error. Try updating Java. ";
                }
                catch(IOException ioEx)
                {
                    exitMsg += "Error " + ++ctExitExcep + ": Failed to fix a file error. Try reinstalling if this message persists. ";
                    
                     /*An IOException might be short-lived or file-related, so still
                    try fixing the preferences .chk file and saving preferences.*/                   
                    runChecksumExitRoutine();
                }
            }
            
            /*No database-file issues to fix*/
            else
                runChecksumExitRoutine();   // Take care of storing preferences
        }
                
        /*A NoSuchAlgorithmException has nothing to do with closing connections,
            so always attempt this*/
        try
        {
            dbUtil.closeDataSource();
        }
        catch(SQLException sqlEx)
        {
            exitMsg += "Error " + ++ctExitExcep + ": The database connection could not be closed. ";
        }
                
        if(ctExitExcep > 0)
        {
                alerter.setAlert
                (Alert.AlertType.ERROR
                    , "Error"
                    , "One or more errors occurred during shutdown. "
                    , exitMsg);
        }
    }
    
    
    /**
     * Persists preferences and attempts to restore preferences from the user's
     * last session, if they couldn't be retrieved because of an IOException.
     */
    private void runChecksumExitRoutine()
    {
        boolean prefsMatch = false;
        
        /*If there was an IOException while first retrieving preferences,
        try to restore them now*/
        if(wasPrefsIO)
        {
            prefsMatch = runCreateCheckRoutine();
        }

        /*Unless the .chk file and .properties files were matched just now,
        overwrite both files*/
        if(!prefsMatch)
            runOverwriteRoutine();
    }
    
    /**
     * Tests a file to determine whether a hash of its contents match the contents
     * in a corresponding .chk file. Concatenates an error message and increments
     * an exception count if exceptions occur.
     * @return true if checksums matched
     */
    private boolean runCreateCheckRoutine()
    {
        boolean prefsMatch = false;
        try
        {
            // If the hashes match, we'll return true, and the program will leave them alone
            prefsMatch = chkUtil.compareChecksums(PREF_FILE);
        }
        
        // Exception while trying to compare checksums
        catch (IOException ex) 
        {
            exitMsg += "Error " + ++ctExitExcep + ": Failed to fix a file error. Try reinstalling if this message persists. ";
            /*IOException might be short-lived, so try rewriting the preferences file*/ 
            runOverwriteRoutine();
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            exitMsg += "Error " + ++ctExitExcep + ": Failed to fix a file error. Try updating Java. ";
            prefsMatch = true; // Prevent more hashing from being attempted
        }
        return prefsMatch;
    }
    
    /**
     * Resets the contents of a file and its corresponding .chk file, to match
     * whatever is in the existing preferences Properties object. Resetting could
     * occur if the integrity of the existing file could not be verified or simply
     * if the user changed their preferences.
     */
    private void runOverwriteRoutine()
    {
        try 
        {
            /*Try to save whatever is in the prefs object and create a 
            .chk file with that data*/
             pu.saveParamChanges(prefs, PREF_FILE);
             chkUtil.createCheckFile(PREF_FILE);
        } 
        catch (NoSuchAlgorithmException algoEx2) 
        {
            exitMsg += "Error " + ++ctExitExcep + ": Preferences could not be saved. Try updating Java. ";
        }
        catch (IOException ioEx2)
        {
            exitMsg += "Error " + ++ctExitExcep + ": Preferences could not be saved. Try reinstalling if this message persists. ";
        }
    }	
	
    /**
     * The main method
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
} //End Class ChordCommand
