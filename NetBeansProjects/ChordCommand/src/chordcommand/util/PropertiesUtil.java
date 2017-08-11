package chordcommand.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
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
    static ChecksumUtil chkUtil;
    
    public PropertiesUtil()
    {
        chkUtil = new ChecksumUtil();        
    }
    
    /**
     * Load and return properties from file @fileName
     * @param fileName
     * @return Properties--Example: database credentials
     * @throws FileNotFoundException
     * @throws IOException 
     * @throws java.security.NoSuchAlgorithmException 
     */
    public Properties loadParams(String fileName) throws FileNotFoundException, IOException, NoSuchAlgorithmException
    {
        boolean isMatch = chkUtil.compareChecksums(fileName);
        
        
        if(isMatch)
        {
            System.out.println("Is a match: " + fileName);
        } 
        else
        {
            System.out.println("Not a match: " + fileName);
        }
        
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
     * @throws FileNotFoundException
     * @throws IOException 
     * @throws java.security.NoSuchAlgorithmException 
     */
    public void saveParamChanges(Properties props, String fileName) 
            throws FileNotFoundException, IOException, NoSuchAlgorithmException
    {
        try(OutputStream out = new FileOutputStream(fileName))
        {
            props.store(out, null);
        }
        chkUtil.createCheckFile(fileName);        
    }
    
} // End class PropertiesUtil
