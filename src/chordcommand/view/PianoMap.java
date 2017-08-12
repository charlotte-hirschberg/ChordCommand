package chordcommand.view;

import java.util.HashMap;

/** 
 * Description: The PianoMap class is simply a wrapper for a HashMap that
 * maps String keys to integers that identify piano keys in the lowest octave.
 * Each value corresponds to multiple keys.
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class PianoMap {
    private static HashMap<String, Integer> pKeys;
    
    /**
     * Populate the HashMap with note names and piano-key IDs
     */
    public PianoMap()
    {
        // To do: add all double sharps
        pKeys = new HashMap<>();
        pKeys.put("C", 1);
        pKeys.put("D♭♭", 1);
        pKeys.put("B#", 1);
        pKeys.put("D♭", 2);
        pKeys.put("C#", 2);
        pKeys.put("D", 3);
        pKeys.put("E♭♭", 3);
        pKeys.put("E♭", 4);
        pKeys.put("D#", 4);
        pKeys.put("F♭♭", 4);
        pKeys.put("E", 5);
        pKeys.put("F♭", 5);
        pKeys.put("F", 6);
        pKeys.put("E#", 6);
        pKeys.put("G♭♭", 6);
        pKeys.put("F#", 7);
        pKeys.put("G♭", 7);
        pKeys.put("G", 8);
        pKeys.put("A♭♭", 8);
        pKeys.put("A♭", 9);
        pKeys.put("G#", 9);
        pKeys.put("A",10 );
        pKeys.put("B♭♭", 10);
        pKeys.put("B♭", 11);
        pKeys.put("A#", 11);
        pKeys.put("C♭♭", 11);
        pKeys.put("B", 12);
        pKeys.put("C♭", 12);
    }
    
    /**
     * Return the piano-key ID with data-key s
     * @param s a pitch name/map key
     * @return 
     */
    public int getKeyID(String s)
    {
        return pKeys.get(s);
    }
} // End class PianoMap
