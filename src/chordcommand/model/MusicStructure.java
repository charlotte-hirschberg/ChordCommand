package chordcommand.model;

import java.util.ArrayList;

/** 
 * @Course: SDEV 435 ~ Applied Software Practice
 * @Author Name: Charlotte Hirschberger
 * @Assignment ChordCommand
 * @Date: Jun 12, 2017
 * @Description: The parent class to Chord and Scale. This defines the
 * characteristics that those musical objects share, including pitches,
 * accidentals (represented by numbers), and a major key (which can then be altered
 * by accidentals to produce other sonorities).
 */
public class MusicStructure {
    private static AccidentalMap am;    // maps integers to accidentals
    private MajorKey mKey;              // the unaltered key
    private String displayName;
    private String strNums;
    private ArrayList<Integer> nums;
    private ArrayList<Integer> suffxs;
    
    private String strPitches;
    
    /**
     * Constructor that sets default values and initializes ArrayLists and
     * a static AccidentalMap member.
     */
    public MusicStructure()
    {
        am = new AccidentalMap();
        mKey = null;
        displayName = "";
        strNums = "";
        strPitches = "";
        nums = new ArrayList<>();
        suffxs = new ArrayList<>();
    }
    
    /**
     * Set member mKey equal to the provided MajorKey argument
     * @param mKey a MajorKey object
     */
    public void setMajKey(MajorKey mKey)
    {
        this.mKey = mKey;
    }
    
    /**
     * Set member displayName equal to the provided string, which should be the
     * user's original chord symbol entry, not the database-friendly one
     * @param origEntry the user's chord symbol entry
     */
    public void setDisplayName(String origEntry)
    {
        displayName = origEntry;
    }
    
    /**
     * Split a string of numeric characters on a delimiter and then convert each
     * character into an integer and insert it in an ArrayList
     * @param dbEntry delimited string of field data
     * @param destination ArrayList to hold split string
     */
    public void fillArr(String dbEntry, ArrayList<Integer> destination)
    {
        String[] splitEnt = dbEntry.split(",");
        
        for (String val : splitEnt) {
            destination.add(Integer.parseInt(val));
        }
    }
    
    /**
     * Fill an ArrayList with the numbers in a comma-delimited string, which
     * represent a chord or scale. Example: 1,3,5,7
     * @param nums the string of integers to be inserted
     */
    public void setNumArr(String nums)
    {
        fillArr(nums, this.nums);
    }
    
    /**
     * Fill an ArrayList with the numbers in a comma-delimited string, which
     * represent the accidentals in a chord.
     * @param suffs the string of integers to be inserted
     */
    public void setSuffsArr(String suffs)
    {
        fillArr(suffs, suffxs);
    }

    /**
     * Use the MusicStructure object's arrays and the AccidentalMap member to
     * create the strings that will be displayed in ChordCommand's output area.
     */
    public void setDisplayVals()
    {
        int ctNotes = 0;    // Track the index in ArrayList @nums
        
        /*Cycle through the numbers in a chord or scale. They may not be
        sequential, as in 1,3,5,7 or 1,2,3,4,5,6,7,7
        */
        for(Integer i : nums)
        {            
            // Get the index of the corresponding pitch name
            int iMod = (i - 1) % 7;
            

            // Get the suffix value (an integer) at this index in nums
            int genAlter = suffxs.get(ctNotes);
            
            /*Add two suffixes and use the sum to determine the accidental
            that occurs when this structure's tonality is applied to the 
            major key. Example: 0 + 1 represents a natural note becoming a sharp*/            
            int alter = mKey.getSuff(iMod) + genAlter; 
            
            // Get the non-key-specific accidental with key genAlter
            String genAccidental = am.getAccidental(genAlter);
            
            // Get the key-specific accidental
            String accidental = am.getAccidental(alter);
            
            // Build numeric structure with accidental + number, i.e. #3
            strNums += genAccidental + String.valueOf(i) + "-";
            
            
            /*Build pitch structure with letter + accidental, i.e. Cb*/  
            // Retrieve the pitch name
            char pitchName = mKey.getPitch(iMod);
            
            strPitches += pitchName + accidental + "-";
            ctNotes++;
        }
        strPitches = strPitches.substring(0, strPitches.length() - 1);
        strNums = strNums.substring(0, strNums.length() - 1);
    }
    
    /**
     * Insert enough space to the right of s to generate a string that is
     * as wide as the value in columns.
     * @param s the string to pad
     * @param columns the desired width of output
     * @return the padded string
     */
    public String padRight(String s, int columns)
    {
        return String.format("%1$-"+columns+ "s", s);
    }
    
    /**
     * Return the string with this MusicStructure's numerical representation
     * @return a string of accidentals and numbers
     */
    public String getStrNums()
    {
        return strNums;
    }
    
    /**
     * Return the string with this MusicStructure's letter-based representation
     * @return a string of accidentals and letters
     */
    public String getStrPitches()
    {
        return strPitches;
    }
    
    /**
     * Get this MusicStructure's MajorKey object
     * @return a MajorKey object
     */
    public MajorKey getMajKey()
    {
        return mKey;
    }
    
    /**
     * Get this MusicStructure's display name
     * @return a name to display
     */
    public String getDisplayName()
    {
        return displayName;
    }
}// End class MusicStructure
