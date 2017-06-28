/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chordcommand;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Charlotte
 */

// TO DO: Combine redundant code in loadParams & load..FromXML
public class PropertiesUtil 
{
    /**
     * Load and return properties from file @fileName
     * @param fileName
     * @return Properties       Example: db credentials
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Properties loadParams(String fileName) throws FileNotFoundException, IOException
    {
        Properties props = new Properties();
        File f = new File(fileName);
        InputStream in = new FileInputStream(f);
        props.load(in);
        
        return props;
    }
    
    public Properties loadParamsFromXML(String fileName) throws FileNotFoundException, IOException
    {
        Properties props = new Properties();
        File f = new File(fileName);
        InputStream in = new FileInputStream(f);
        props.loadFromXML(in);
        
        return props;
    }
    
    public void saveParamChanges(HashMap<String,String> hm, String fileName) 
            throws FileNotFoundException, IOException
    {
        Properties props = new Properties();
        Set<Map.Entry<String,String>> entries = hm.entrySet();
        entries.stream().forEach((entry) -> {
            props.setProperty(entry.getKey(), entry.getValue());
        });
        OutputStream out = new FileOutputStream(new File(fileName));
        props.store(out, null);
    }
    
}
