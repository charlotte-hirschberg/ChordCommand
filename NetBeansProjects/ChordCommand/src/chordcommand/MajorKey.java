/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chordcommand;

/**
 *
 * @author Charlotte
 */
public class MajorKey {
private String keyName;
private char[] majorPitches;
private int[] majorSuffixes;
private int shift;

/**
 * Constructor: Build a new object with the given name and fill its pitch array
 * @param name 
 */
    public MajorKey(String name)
    {
        keyName = name;
        majorPitches = new char[8];
        majorSuffixes = new int[8];
        setShift(keyName.charAt(0));
        setPitches();
    }
    
    
    public void setShift(char letter)
    {
        // Get displacement of @letter from A
        // Example: C = 67, 67 % 65 = 2, C is 2 away from A
        int start = (int)letter;
        shift = start % 65;
    }
    
    public int getShift()
    {    
        return shift;
    }
    
    /**
     * Use the first character in the key to fill this MajorKey's pitch array.
     * Each index will hold a single character A-G. If the first character is C,
     * for example, the array contents will cycle back to A, giving {C,D,E,F,G,A,B}
     * @param letter A-G
     */
    public void setPitches()
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
     * @param suffixes 
     */
    public void setSuffixes(String suffixes)
    {
        String[] suffs = suffixes.split(",");
        
        for(int i = 0; i < 7; i++)
            majorSuffixes[i] = Integer.valueOf(suffs[i]);
    }
    
    public char getPitch(int i)
    {
        return majorPitches[i];
    }
    
    public int getSuff(int i)
    {
        return majorSuffixes[i];
    }
    
}
