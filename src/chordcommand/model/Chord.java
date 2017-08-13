package chordcommand.model;

import java.util.ArrayList;

/** 
 * Description: Sub class of MusicStructure. This Chord adds a list of Scale
 * objects, an ID number, and the chord symbol recognized by the database.
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class Chord extends MusicStructure {
    private String dbName;  // Chord symbol recognized by database
    private int id;
    private ArrayList<Scale> scales;
    
    /**
     * Constructor that initializes a default Chord object.
     */
    public Chord()
    {
        super();
        dbName = "";
        id = -1;
        scales = new ArrayList<>();
    }
    
    /**
     * Set dbName to hold the symbol recognized by the database, parsed from
     * the user's original entry.
     * @param entry  the user's entry after conversion to database-friendly form
     */
    public void setDBName(String entry)
    {
        dbName = entry;
    }
    
    /**
     * Set ID to the number that identifies a chord with this quality and
     * extension in the database.
     * @param id corresponding ChordID in database, may be shared by other Chords
     */
    public void setID(int id)
    {
        this.id = id;
    }
    
    /**
     * Add a scale object to this Chord object's list of them
     * @param scale1 a Scale object
     */
    public void addScale(Scale scale1)
    {
        scales.add(scale1);
    }
    
    /**
     * Get the ChordID identifying a chord with this extension and quality
     * in the database
     * @return an integer that correlates to a database ChordID
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Get the scales associated with this Chord
     * @return the list of scales for this Chord
     */
    public ArrayList<Scale> getScaleList()
    {
        return scales;
    }
}
