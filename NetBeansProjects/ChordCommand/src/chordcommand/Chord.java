/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chordcommand;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.transform.Scale;

/**
 *
 * @author Charlotte
 */
public class Chord {
    private MajorKey mKey;
    private String dbName;
    private String displayName;
    private List<Scale> scales;
    private String strChordNums;
    private ArrayList<Integer> chordNums;
    private ArrayList<Integer> chordSuffs;
    private String strPitches;
    
    public Chord()
    {
        mKey = null;
        dbName = "";
        displayName = "";
        scales = new ArrayList<>();
        strPitches = "";
    }
    
    public void setMajKey(MajorKey mKey)
    {
        this.mKey = mKey;
    }
    
    public void setDBName(String entry)
    {
        dbName = entry;
    }
    
    public void setDisplayName(String origEntry)
    {
        displayName = origEntry;
    }
    
    public void setChordNums(String nums)
    {
        strChordNums = nums;
    }
    
    public void setChordNumArr(String nums)
    {
        for(int i = 0; i < nums.length(); i++)
        {
            char curr = nums.charAt(i);
            if(curr != ',')
                chordNums.add((int)curr);
        }
    }
    
    public void setChordSuffsArr(String suffs)
    {
        String[] strSuffs = suffs.split(",");
        for(int i = 0; i < strSuffs.length; i++)
            chordNums.add(Integer.valueOf(strSuffs[i]));
    }
    
    public void setPitches()
    {
        for(Integer i : chordNums)
        {
            char pitchName = mKey.getPitch(i);
            strPitches += String.valueOf(pitchName);
            int alter = mKey.getSuff(i) + i;
            strPitches += translateSuff(alter);
        }
    }
    
    private String translateSuff(int alter)
    {
        // Get the map value for key @alter
    }
    
}
