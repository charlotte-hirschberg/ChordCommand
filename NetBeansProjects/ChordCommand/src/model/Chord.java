/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author Charlotte
 */
public class Chord extends MusicStructure {
    private String dbName;
    private int id;
    private ArrayList<Scale> scales;
    
    public Chord()
    {
        super();
        dbName = "";
        id = -1;
        scales = new ArrayList<>();
    }
    
    public void setDBName(String entry)
    {
        dbName = entry;
    }
    
    public void setID(int id)
    {
        this.id = id;
    }
    
    public void addScale(Scale scale1)
    {
        scales.add(scale1);
    }
    
    public int getId()
    {
        return id;
    }
    
    public ArrayList<Scale> getScaleList()
    {
        return scales;
    }
}
