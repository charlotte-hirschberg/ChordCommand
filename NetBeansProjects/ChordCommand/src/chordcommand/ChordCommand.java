package chordcommand;

/** 
 * @Course: SDEV 350 ~ Java Programming I
 * @Author Name: Charlotte Hirschberger
 * @Assignment Name: chordcommand
 * @Date: Jun 12, 2017
 * @Description: 
 */

//Imports
import chordcommand.view.ChordViewController;
import chordcommand.view.HelpViewController;
import chordcommand.view.PrefDialogController;
import chordcommand.view.RootLayoutController;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//Begin Class ChordCommand
public class ChordCommand extends Application {
    private Stage primaryStage;
    private ScrollPane rootLayout;
    private HBox centerPane;
    private DBUtil dbUtil;
    private ObservableList<String> instrComboData = FXCollections.observableArrayList();
    private ObservableList<String> keyComboData = FXCollections.observableArrayList();

    private final static String PREF_FILE = "userprefs.properties";
    private PropertiesUtil pu;
    private Properties prefs;
    private Scene scene;
    RootLayoutController rootCtrl;

    @Override
    public void start(Stage primaryStage) 
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Chord Command"); 
        
        initDB();
        initRootLayout();
        setComboData();
        getPrefs();
        showChordView();
        
        // Show help view only if it is enabled to show up on startup
        if("1".equals(prefs.getProperty("showHelp")))
            showHelpView();
        
        
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
                    Logger.getLogger(ChordCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
    }
    
    public Scene getScene()
    {
        return scene;
    }
    
    /**
     * Get a user's preferences and create a Properties object with them
     */
    public void getPrefs()
    {
        pu = new PropertiesUtil();
        try 
        {
            prefs = pu.loadParams(PREF_FILE);
        } 
        catch (IOException ex) {
            Logger.getLogger(ChordCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setSinglePref(String key, String value)
    {
        prefs.setProperty(key, value);
    }
    
    public String getSinglePref(String key)
    {
        return prefs.getProperty(key);
    }
    
    public boolean getBooleanProp(String key)
    {
        return Boolean.parseBoolean(getSinglePref(key));
    }
    
    public double getStageWidth()
    {
        return primaryStage.getWidth();
    }
    
    /**
     * Retrieve data from DB and load it in lists for use in ComboBoxes
     */
    public void setComboData()
    {
        String query1 = "SELECT Instrument FROM TransposingKeyLU";
        String query2 = "SELECT MajorKeyName FROM MajorKeyLU ";
        
        try {

            instrComboData = dbUtil.toList(query1, "Instrument");
            keyComboData = dbUtil.toList(query2, "MajorKeyName");
        } 
        catch (SQLException ex) {
            Logger.getLogger(ChordCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns the transposing keys as an observable list of Strings
     * @return 
     */
    public ObservableList<String> getKeyComboData() 
    {
        return keyComboData;
    }
        
    /**
     * Returns the transposing instrument names as an observable list of Strings
     * @return 
     */
    public ObservableList<String> getInstrComboData() 
    {
        return instrComboData;
    }
    
    /**
     * Initializes a set of Properties with database credentials
     */
    public void initDB()
    {
        try
	{
            dbUtil = new DBUtil();
	}
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the root layout, a GridPane that holds icons and chord input/output
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ChordCommand.class.getResource("view/RootLayout.fxml"));
            rootLayout = (ScrollPane) loader.load();

            // Show the scene containing the root layout.
            scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setMaximized(true);
            
            rootCtrl = loader.getController();
            centerPane = rootCtrl.getCenterPane();
            rootCtrl.setMainApp(this);
            rootCtrl.setGridWidth(primaryStage.widthProperty());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showHelpView() 
    {
            try 
            {
                // Load person overview.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(ChordCommand.class.getResource("view/HelpView.fxml"));
                AnchorPane helpView = (AnchorPane) loader.load();
            
                // Create the dialog Stage.
                Stage helpStage = new Stage();
                helpStage.setTitle("Quick Start Guide");
                helpStage.initOwner(primaryStage);
                Scene helpScene = new Scene(helpView);
                helpStage.setScene(helpScene);
                helpStage.show();            

                // Set the person into the controller.
                HelpViewController controller = loader.getController();
                controller.setHelpStage(helpStage);
                controller.setMainApp(this);
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
    }
        
    public void showChordView() {
        try {
            // Load chord controls & output layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ChordCommand.class.getResource("view/ChordView.fxml"));
            VBox chordView = (VBox)loader.load();
            
            centerPane.getChildren().add(chordView);
            
            ChordViewController ctrller = loader.getController();
            ctrller.setMainApp(this);
            ctrller.setCombos();
            ctrller.initOutputArea();
            ctrller.setRootCtrl(rootCtrl);
            rootCtrl.setChordCtrl(ctrller);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showPrefView()
    {
        try 
        {
            // Load chord controls & output layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ChordCommand.class.getResource("view/PrefDialog.fxml"));
            AnchorPane prefDialog = (AnchorPane)loader.load();
            
            // Create the dialog Stage.
            Stage prefStage = new Stage();
            prefStage.setTitle("Set Preferences");
            prefStage.initOwner(primaryStage);
            Scene prefScene = new Scene(prefDialog);
            prefStage.setScene(prefScene);
            prefStage.show();
            
            PrefDialogController ctrller = loader.getController();
            ctrller.setMainApp(this);
            ctrller.setHelpStage(prefStage);
            ctrller.initCBValues();
        } 
        
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public void causeExit()
    {
        primaryStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
} //End Class ChordCommand