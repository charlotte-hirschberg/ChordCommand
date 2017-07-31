/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Charlotte
 */
public class Scale extends MusicStructure {
    private String whForm;
    
    public Scale()
    {
        super();
        this.whForm = "";
    }
    
    public Scale(String name, String whForm, MajorKey mKey)
    {
        super();
        this.whForm = whForm;
        super.setDisplayName(name);
        super.setMajKey(mKey);
    }
    
    public String getStrWH()
    {
        return whForm;
    }
}
