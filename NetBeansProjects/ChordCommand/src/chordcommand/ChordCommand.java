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
import chordcommand.view.RootLayoutController;
import java.io.IOException;
import java.sql.SQLException;
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
import javafx.stage.Stage;

//Begin Class ChordCommand
public class ChordCommand extends Application {
    private Stage primaryStage;
    private ScrollPane rootLayout;
    private HBox centerPane;
    private DBUtil dbUtil;
    private ObservableList<String> instrComboData = FXCollections.observableArrayList();
    private ObservableList<String> pitchComboData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) 
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Chord Command"); 
        
        initDB();
        initRootLayout();
        setCombos();
        showHelpView();
        showChordView();
    }
    
    /**
     * Retrieve data from DB and load it in lists for use in ComboBoxes
     */
    public void setCombos()
    {
        String query1 = "SELECT Instrument FROM TransposingKeyLU";
        String query2 = "SELECT MajorKeyName FROM MajorKeyLU as mk JOIN TransposingKeyLU as tk"
                    + " ON mk.MajorKeyID = tk.MajorKeyID GROUP BY MajorKeyName";
        
        try {

            instrComboData = dbUtil.toList(query1, "Instrument");
            pitchComboData = dbUtil.toList(query2, "MajorKeyName");
        } 
        catch (SQLException ex) {
            Logger.getLogger(ChordCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Returns the transposing keys as an observable list of Strings
     * @return 
     */
    public ObservableList<String> getPitchComboData() 
    {
        return pitchComboData;
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
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            RootLayoutController ctrller = loader.getController();
            centerPane = ctrller.getCenterPane();
            ctrller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
        public void showHelpView() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ChordCommand.class.getResource("view/HelpView.fxml"));
            AnchorPane helpView = (AnchorPane) loader.load();
            
            // Create the dialog Stage.
            Stage helpStage = new Stage();
            helpStage.setTitle("Quick Start Guide");
            helpStage.initOwner(primaryStage);
            Scene scene = new Scene(helpView);
            helpStage.setScene(scene);
            helpStage.show();            

    // Set the person into the controller.
            HelpViewController controller = loader.getController();
            controller.setHelpStage(helpStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    public void showChordView() {
        try {
            // Load chord controls & output layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ChordCommand.class.getResource("view/ChordView.fxml"));
            AnchorPane chordView = (AnchorPane) loader.load();
            
            centerPane.getChildren().add(chordView);
            
            ChordViewController ctrller = loader.getController();
            ctrller.setMainApp(this);
            ctrller.setCombos(instrComboData, pitchComboData);
            
        } catch (IOException e) {
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

    public static void main(String[] args) {
        launch(args);
    }
} //End Class ChordCommand
