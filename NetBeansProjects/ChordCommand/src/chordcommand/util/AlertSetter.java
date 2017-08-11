package chordcommand.util;

/** 
 * Description: Constructs and displays Alert using given parameters
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * <p>Created Date: Feb 28, 2016</p>
 * Last Update: 8/2/17 
 */

//Imports
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

//Begin Class AlertSetter
public class AlertSetter
{
    Optional<ButtonType> result = null;//for use if Alert is Confirmation type
    
    /**
     * Set an Alert with the desired properties.
     * @param alType type of Alert
     * @param title dialog title
     * @param header 
     * @param content description of error, choice, or important info
     */
    public void setAlert(Alert.AlertType alType, String title, String header, String content)
    {
        Alert al = new Alert(alType);
        al.setTitle(title);
        al.setHeaderText(header);
        al.setContentText(content);
        
        //store user's choice (OK or Cancel) in result
        result = al.showAndWait();
    }
    
    /**
     * Output user's choice of button in the Alert
     * @return user's choice
     */
    public Optional<ButtonType> getChoice()
    {
        return result;    
    }
}//End Class AlertSetter
