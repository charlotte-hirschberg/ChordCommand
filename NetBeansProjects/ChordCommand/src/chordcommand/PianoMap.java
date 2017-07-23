package chordcommand;

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
public class PianoMap {
    private static HashMap<String, Integer> pKeys;
    
    /**♭
     * Populate the HashMap
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
    
    public int getKeyID(String s)
    {
        return pKeys.get(s);
    }
}
