/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chordcommand.view;

import chordcommand.ChordCommand;
import chordcommand.ChordUtil;
import chordcommand.Chord;
import chordcommand.MajorKey;
import chordcommand.PianoMap;
import chordcommand.Scale;
import java.sql.SQLException;
import java.text.Normalizer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Charlotte
 */
public class ChordViewController {

    @FXML
    private TextField symbolTF;
    @FXML
    private ComboBox<String> instrCombo;
    private ObservableList<String> instrComboData = FXCollections.observableArrayList();
    @FXML
    private TextArea chordTA;
    @FXML
    private TreeView scaleTree;
    @FXML
    private Pane pianoPane;
    
    private ChordCommand main;
    private ChordUtil cu;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() 
    {
    }

    public void setCombo(ObservableList<String> data1, String title)
    {
        instrCombo.setItems(data1);
        instrCombo.setValue(title);
    }
    
        /**
     * Called by main, so this class has a reference to it
     * @param main 
     */
    public void setMainApp(ChordCommand main)
    {
        this.main = main;
    }
    
    @FXML
    private void handleClick(MouseEvent event)
    {
        Button b1 = (Button)event.getSource();
        symbolTF.appendText(b1.getText());
    }
    
    @FXML
    private void handleSubmit()
    {
        String entry = symbolTF.getText();
        String origEntry = entry;
        MajorKey mKey = null;
        Chord chord1;
        
        if(!entry.equals(""))
        {
            try
            {
                cu = new ChordUtil();
                if(cu.isValidInput(entry, "^[A-G]?[b♭]?[majsuinbd1-9\\+\\-#°ø♭Δ]{1,12}$", Normalizer.Form.NFD))
                {
                    String key = cu.extractKey(entry);
                    if(key != null)
                    {
                        mKey = cu.buildKey(key);
                        if(key.length() == 2)
                            entry = entry.substring(2);
                        else
                            entry = entry.substring(1);
                    }
                    // Regardless, proceed with building chord
                    entry = cu.formatSymbol(entry);
                    chord1 = cu.buildChord(entry, mKey);
                    
                    if(chord1 != null)
                    {
                        chord1.setDisplayName(origEntry);
                        showChordDetails(chord1);
                        
                        cu.buildScales(chord1);
                        showScaleDetails(chord1);
                        
                        showPiano(chord1);
                    }
                }
                else
                {
                    // Highlight illegal char
                    System.out.println("Illegal char");
                }
                
            }
            catch(SQLException ex)
            {
                System.out.println("SQLException");
            }
        }
        else
        {
            // Highlight missing entry
        }
    }
    
    private void showChordDetails(Chord chord)
    {
        if(chord != null)
        {
            chordTA.setText("Numerical:\t");
            chordTA.appendText(chord.getStrNums());
            chordTA.appendText("\nPitches:\t\t");
            chordTA.appendText(chord.getStrPitches());
        }
    }
    
    
    private void showScaleDetails(Chord chord)
    {
        if(chord != null)
        {
            // Create an empty root that will be hidden
            TreeItem<String> root = new TreeItem<>("");
            root.setExpanded(true);
            
            /*Create root's children. Each child is a scale name, which in
            turn has a string of pitches and a string of numbers/accidentals
            as its children
            */
            for(Scale sc : chord.getScaleList())
            {
                TreeItem<String> name = new TreeItem<>(sc.getDisplayName());
                
                    TreeItem<String> pitches = 
                        new TreeItem<>("Pitches:\t\t" + sc.getStrPitches());
                    TreeItem<String> nums =
                        new TreeItem<>("Numerical:\t" + sc.getStrNums());
                name.getChildren().add(pitches);
                name.getChildren().add(nums);
                
                root.getChildren().add(name);
            }
            scaleTree.setRoot(root);
            scaleTree.setShowRoot(false);
        }
    }
    

    /**
     * Use the chord pitches to display red circles and pitch names to
     * indicate the keys that compose a given chord on the piano
     * @param chord1 
     */
    private void showPiano(Chord chord1)
    {
        PianoMap pKeys = new PianoMap();
        String[] pitches = chord1.getStrPitches().split(",");
        int prevId = 0;
        int id;
        
        for(String p : pitches)
        {
            id = pKeys.getKeyID(p);
            
            // Account for octave
            if(id < prevId)
                id += 12;
            
            prevId = id;

            // Get the Rectangle with this ID
            Rectangle rect = (Rectangle)main.getScene().lookup("#" + id);
            double x = rect.getLayoutX();
            double y = rect.getLayoutY();
            double hCenter = rect.getWidth()/2 + x;
            double vCoord = y + rect.getHeight() - 40;
            
            Label pitch = new Label(p);
            pitch.setLayoutX(hCenter - 6);
            pitch.setLayoutY(vCoord + 15);
            
            Circle circ = new Circle(hCenter, vCoord, 10);
            circ.setFill(Color.RED);
            pianoPane.getChildren().add(circ);
            pianoPane.getChildren().add(pitch);
        }
    }
}
