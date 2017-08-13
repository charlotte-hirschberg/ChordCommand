package chordcommand.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/** 
 * Description: The PropertiesUtil class contains methods for creating
 * Properties objects from file content and for persisting Properties in files.
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class PropertiesUtil 
{
    /**
     * Load and return properties from file @fileName
     * @param fileName
     * @return Properties--Example: database credentials
     * @throws IOException 
     */
    public Properties loadParams(String fileName) throws FileNotFoundException, IOException
    {
        Properties props = new Properties();
        
        try(InputStream in = new FileInputStream(fileName))
        {
            props.load(in); 
        }
        
        return props;
    }
    
    /**
     * Persist Properties in a file.
     * @param props a Properties object
     * @param fileName the name of a file to persist changes in
     * @throws IOException 
     */
    public void saveParamChanges(Properties props, String fileName) 
            throws IOException
    {
        try(OutputStream out = new FileOutputStream(fileName))
        {
            props.store(out, null);
        }  
    }
    
} // End class PropertiesUtil
