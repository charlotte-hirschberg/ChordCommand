package chordcommand;

/** 
 * @Course: SDEV 350 ~ Java Programming I
 * @Author Name: Charlotte Hirschberger
 * @Assignment Name: chordcommand
 * @Date: Jun 12, 2017
 * @Description: 
 */

//Imports
import chordcommand.view.HelpViewController;
import chordcommand.view.RootLayoutController;
import java.io.IOException;
import javafx.application.Application;
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

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Chord Command");

        initRootLayout();
        showHelpView();
        showChordView();
    }

    /**
     * Initializes the root layout.
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
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ChordCommand.class.getResource("view/ChordView.fxml"));
            AnchorPane chordView = (AnchorPane) loader.load();
            
            centerPane.getChildren().add(chordView);
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
