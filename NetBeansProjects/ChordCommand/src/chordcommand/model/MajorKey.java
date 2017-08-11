package chordcommand.model;

/** 
 * Description: The MajorKey uses a 1-2 letter String to first construct two
 * arrays that represent the MajorKey's pitches and accidentals respectively.
 * The majorPitches array takes the form of 6 letters in the range A-G. The
 * majorSuffixes array takes the form of numbers -2-2.
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class MajorKey {
    private String keyName;
    private char[] majorPitches;
    private int[] majorSuffixes;
    private int shift;

    /**
     * Constructor: Build a new MajorKey object with the given name and fill 
     * its pitch and suffix arrays.
     * @param name 1-2 letters representing the key, i.e. Bb
    */
    public MajorKey(String name)
    {
        keyName = name;
        majorPitches = new char[8];
        majorSuffixes = new int[8];
        setShift(keyName.charAt(0));
        setPitches();
    }
    
    /**
     * Compute the difference between this letter's ASCII code and the letter
     * A's ASCII code. This is used to dynamically construct an array filled
     * with the names of the pitches in a scale of this key.
     * Example: C = 67, 67 % 65 = 2, C is 2 away from A
     * @param letter 
     */
    private void setShift(char letter)
    {
        int start = (int)letter;
        shift = start % 65;
    }
    
    /**
     * Return the difference between the key's ASCII and A's ASCII code
     * @return the difference in ASCII codes
     */
    public int getShift()
    {    
        return shift;
    }
    
    /**
     * Use the first character in the key to fill this MajorKey's pitch array.
     * Each index will hold a single character A-G. If the first character is C,
     * for example, the array contents will cycle back to A, giving {C,D,E,F,G,A,B}
     * @param letter a letter, A-G, representing the key without any accidental
     */
    private void setPitches()
    {
        int ascii;        
        
        for(int i = 0; i < 7; i++)
        {
            // 65 plus value from 0 - 7
            ascii = 65 + (shift % 7);
            majorPitches[i] = (char)ascii;
            shift++;
        }
    }
    
    /**
     * Convert a comma-separated list of integers to an integer array
     * @param suffixes a comma-separated list of integers that map to accidentals
     */
    public void setSuffixes(String suffixes)
    {
        String[] suffs = suffixes.split(",");
        
        for(int i = 0; i < 7; i++)
            majorSuffixes[i] = Integer.valueOf(suffs[i]);
    }
    
    /**
     * Return a character, A-G, at index i
     * @param i index
     * @return char at index i
     */
    public char getPitch(int i)
    {
        return majorPitches[i];
    }
    
    /**
     * Return an integer, -2 to 2, at index i
     * @param i index
     * @return integer at index i
     */
    public int getSuff(int i)
    {
        return majorSuffixes[i];
    }
    
}// End class MajorKey
