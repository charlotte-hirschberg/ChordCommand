package chordcommand.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/** 
 * @Course: SDEV 435 ~ Applied Software Practice
 * @Author Name: Charlotte Hirschberger
 * @Assignment ChordCommand
 * @Date: Jun 12, 2017
 * @Description: The PropertiesUtil class contains methods for creating
 * Properties objects from file content and for persisting Properties in files.
 */
public class PropertiesUtil 
{
    /**
     * Load and return properties from file @fileName
     * @param fileName
     * @return Properties--Example: database credentials
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Properties loadParams(String fileName) throws FileNotFoundException, IOException
    {
        Properties props = new Properties();
        
        InputStream in = chordcommand.ChordCommand.class.getResourceAsStream(fileName);
        props.load(in);
    
        
        return props;
    }
    
    /**
     * Persist Properties in a file.
     * @param props a Properties object
     * @param fileName the name of a file to persist changes in
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void saveParamChanges(Properties props, String fileName) 
            throws FileNotFoundException, IOException
    {
        OutputStream out = new FileOutputStream(new File(fileName));
        props.store(out, null);
    }
    
} // End class PropertiesUtil
