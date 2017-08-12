package chordcommand.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/** 
 * Description: The KeyMarker class creates a Label and Circle displayed on a
 * piano key. It derives the KeyMarker's layout attributes from the corresponding
 * Rectangle's position.
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class KeyMarker extends VBox
{
    private Label note;
    private Circle circ;
    private final static Color CIRC_FILL  = Color.RED;
    private final static int RADIUS = 8;        // Circle radius
    private final int VCOORD_TRANSLATE = -40;
    
    public KeyMarker(Rectangle rect, String pitch)
    {
        this.setAlignment(Pos.CENTER);
        
        // Get Rectangel position
        double x = rect.getLayoutX();
        double y = rect.getLayoutY();
        
        // Position relative to base of Rectangle
        this.setLayoutY(y + rect.getHeight() + VCOORD_TRANSLATE);
        this.setPrefWidth(rect.getWidth());
        this.setLayoutX(x);
                
            
        note = new Label(pitch);
        circ = new Circle(RADIUS);
        circ.setFill(CIRC_FILL);
        note.setTextFill(CIRC_FILL);
            
        this.getChildren().add(circ);
        this.getChildren().add(note);
    }
} // End class KeyMarker
