package chordcommand.model;

import java.util.HashMap;

/** 
 * @Course: SDEV 435 ~ Applied Software Practice
 * @Author Name: Charlotte Hirschberger
 * @Assignment ChordCommand
 * @Date: Jun 12, 2017
 * @Description: This is essentially a wrapper for a HashMap with Integer key
 * and String value. It defines the correspondence between integers -2 to 2 and
 * accidentals.
 */
public class AccidentalMap {
    private static HashMap<Integer, String> accidentals;
    
    /**
     * Constructor that populates the HashMap
     */
    public AccidentalMap()
    {
        accidentals = new HashMap<>();
         accidentals.put(-2, "♭♭");
         accidentals.put(-1, "♭");
         accidentals.put(0, "");
         accidentals.put(1, "#");
         accidentals.put(2, "");
    }
    
    /**
     * Essentially a wrapper that returns the value for key @i
     * @param i     Integer in range -2-2 (inclusive)
     * @return      Value corresponding to Key i
     */
    public String getAccidental(int i)
    {
        return accidentals.get(i);
    }
}// End class AccidentalMap