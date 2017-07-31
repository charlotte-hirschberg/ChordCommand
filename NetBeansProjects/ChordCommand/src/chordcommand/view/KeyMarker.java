/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chordcommand.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Charlotte
 */
public class KeyMarker extends VBox
{
    private Label note;
    private Circle circ;
    private final static Color CIRC_FILL  = Color.RED;
    private final static int RADIUS = 8;
    private final int VCOORD_TRANSLATE = -40;
    
    public KeyMarker(Rectangle rect, String pitch)
    {
        this.setAlignment(Pos.CENTER);
        
        double x = rect.getLayoutX();
        double y = rect.getLayoutY();
        
        this.setLayoutY(y + rect.getHeight() + VCOORD_TRANSLATE);
        this.setPrefWidth(rect.getWidth());
        this.setLayoutX(x);
                
            
        note = new Label(pitch);
        circ = new Circle(RADIUS);
        circ.setFill(CIRC_FILL);
            
        this.getChildren().add(circ);
        this.getChildren().add(note);
    }
}
