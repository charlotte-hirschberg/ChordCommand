package model;

import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Charlotte
 */
public class AccidentalMap {
    private static HashMap<Integer, String> accidentals;
    
    /**
     * Populate the HashMap
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
}
