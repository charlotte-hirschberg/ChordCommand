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
    
        /**Returns a prepared alert, so the calling method can take care of displaying
     * it. Example: calling method populates the dialog with custom buttons.
     * @param alType Alert.AlertType- error, confirmation, etc
     * @param title dialog title
     * @param header heading within dialog
     * @param content description of error
     * @return 
     */
    public Alert getAlert(Alert.AlertType alType, String title, String header, String content)
    {
        Alert al = new Alert(alType);
        al.setTitle(title);
        al.setHeaderText(header);
        al.setContentText(content);
        
        return al;
    }
    
    /**
     * Puts Yes and No buttons in an alert dialog and causes the No button
     * to trigger System.exit.
     * @param al the alert object
     */
    public void setYesNo(Alert al)
    {           
        ButtonType typeYes = new ButtonType("Yes");
        ButtonType typeNo = new ButtonType("No");
            
        al.getButtonTypes().setAll(typeYes, typeNo);
        Optional<ButtonType> result2 = al.showAndWait();
            
        if(result2.get() == typeNo)
            System.exit(1);
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
