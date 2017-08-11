package chordcommand.model;

/** 
 * @Course: SDEV 435 ~ Applied Software Practice
 * @Author Name: Charlotte Hirschberger
 * @Assignment ChordCommand
 * @Date: Jun 12, 2017
 * @Description: An immediate subclass of MusicStructure. Defines the traits
 * that differentiate a scale from a chord.
 */
public class Scale extends MusicStructure {
    private String whForm;
    
    /**
     * Default constructor. Calls the superclass constructor and sets whForm to
     * empty
     */
    public Scale()
    {
        super();
        this.whForm = "";
    }
    
    /**
     * Constructor that accepts 3 parameters
     * @param name a string like "Minor"
     * @param whForm a string of W's, H's, and '-3'
     * @param mKey a MajorKey object
     */
    public Scale(String name, String whForm, MajorKey mKey)
    {
        super();
        this.whForm = whForm;
        super.setDisplayName(name);
        super.setMajKey(mKey);
    }
    
    /**
     * Return the whole-half step representation of this scale. It will be a
     * mix of spaces, '-3' strings, W's, and H's
     * @return a string represent the whole-half notation
     */
    public String getStrWH()
    {
        return whForm;
    }
}// End class Scale
