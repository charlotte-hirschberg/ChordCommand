/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chordcommand;

import java.util.ArrayList;

/**
 *
 * @author Charlotte
 */
public class MusicStructure {
    private static AccidentalMap am;
    private MajorKey mKey;
    private String displayName;
    private String strNums;
    private ArrayList<Integer> nums;
    private ArrayList<Integer> suffxs;
    private String strPitches;
    
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
    
    
    public void setMajKey(MajorKey mKey)
    {
        this.mKey = mKey;
    }
    
    public void setDisplayName(String origEntry)
    {
        displayName = origEntry;
    }
    
    public void fillArr(String dbEntry, ArrayList<Integer> destination)
    {
        String[] splitEnt = dbEntry.split(",");
        
        for (String val : splitEnt) {
            destination.add(Integer.parseInt(val));
        }
    }
    
    public void setNumArr(String nums)
    {
        fillArr(nums, this.nums);
    }
    
    public void setSuffsArr(String suffs)
    {
        fillArr(suffs, suffxs);
    }
    
    public void setDisplayVals()
    {
        int ctNotes = 0;
        for(Integer i : nums)
        {
            // Get the index of the corresponding pitch name
            int iMod = (i - 1) % 7;
            
            /*Add two suffixes and use the sum to determine the accidental
            that occurs when this structure's tonality is applied to the 
            major key*/
            int alter = mKey.getSuff(iMod) + suffxs.get(ctNotes);
            String accidental = am.getAccidental(alter);
            
            // Build numeric structure with accidental + number, i.e. #3
            strNums += accidental;
            strNums += String.valueOf(i) + " ";
            
            
            /*Build pitch structure with letter + accidental, i.e. Cb*/  
            // Retrieve the pitch name
            char pitchName = mKey.getPitch(iMod);
            
            // Append the pitch name and accidental
            strPitches += String.valueOf(pitchName) + accidental + " ";
            ctNotes++;
        }
    }
    
    public String getStrNums()
    {
        return strNums;
    }
    
    public String getStrPitches()
    {
        return strPitches;
    }
    
    public MajorKey getMajKey()
    {
        return mKey;
    }
    
    public String getDisplayName()
    {
        return displayName;
    }
}
