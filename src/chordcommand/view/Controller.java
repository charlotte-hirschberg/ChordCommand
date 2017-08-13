package chordcommand.view;

import javafx.application.Application;
import javafx.fxml.FXML;

/** 
 * Description: Base Controller class that allows the derived controller classes
 * to be used in one loadFXML method, despite their otherwise unique composition.
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class Controller 
{
    protected Application mainApp;
    
    /**Automatically called when FXML is loaded.
     */
    @FXML
    private void initialize()
    {
        
    }
    
    /**
     * Give this Controller a reference to the main Application class
     * @param main the Application to provide a reference to
     */
    public void setMainApp(Application main)
    {
        mainApp = main;
    }
}
