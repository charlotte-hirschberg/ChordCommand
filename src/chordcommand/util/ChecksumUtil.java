
package chordcommand.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/** 
 * Description: This class contains modules for creating and comparing checksums
 * for the purpose of verifying file authenticity. Adapted from 
 * http://www.rgagnon.com/javadetails/java-0416.html
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Aug. 10, 2017
 */
public class ChecksumUtil 
{
 
    /**
     * Create or overwrite a .chk file that holds the checksum generated from
     * another file.
     * @param fileName  the name for the new .chk file
     * @throws IOException
     * @throws FileNotFoundException
     * @throws NoSuchAlgorithmException 
     */
    public void createCheckFile(String fileName) throws IOException, FileNotFoundException, NoSuchAlgorithmException
    {
        try (OutputStream os = new FileOutputStream(new File(fileName + ".chk")))
        {
            byte[] chk = createChecksum(fileName);
            os.write(chk);
        }
    }

 /**
  * Compare the checksum generated from a file to the checksum stored in a .chk
  * file
  * @param fileName the name of the file to hash
  * @return true if checksums match
  * @throws IOException
  * @throws FileNotFoundException
  * @throws NoSuchAlgorithmException 
  */
    public boolean compareChecksums(String fileName) throws IOException, FileNotFoundException, NoSuchAlgorithmException
    {
        boolean isMatch;
        File f = new File(fileName + ".chk");
        try (InputStream is = new FileInputStream(f))
        {
            byte[] chk1 = createChecksum(fileName);
            byte[] chk2 = new byte[chk1.length];

            is.read(chk2);

            isMatch = new String(chk2).equals(new String(chk1));
        }
        return isMatch;
    }

  /**
   * Use the provided algorithm to convert a file to a hashed value.
   * @param fileName    the name of a file to hash
   * @return the hash as an array of bytes
   * @throws FileNotFoundException
   * @throws IOException
   * @throws NoSuchAlgorithmException 
   */
    public byte[] createChecksum(String fileName) throws FileNotFoundException, IOException, NoSuchAlgorithmException
    {
        MessageDigest md;
        try (InputStream fis = new FileInputStream(fileName)) 
        {
            byte[] buffer = new byte[1024];
            md = getAvailableDigest();
            
            if(md != null)
            {
                int numRead;
            
                while ((numRead = fis.read(buffer)) != -1) 
                {
                    md.update(buffer, 0, numRead);
                }
            }
            else
                throw new NoSuchAlgorithmException();

        }
        return md.digest();
    }
    
    /**
     * Test if a given hashing/checksum algorithm is available for this JVM.
     * @param algo a hashing algorithm, like MD5 or SHA-1
     * @return true if JVM has this algorithm
     */
    public boolean isMDAvailable(String algo)
    {
        boolean success=true;
        try
        {
            MessageDigest.getInstance(algo);
        }
        catch(NoSuchAlgorithmException ex)
        {
            success=false;
        }
        return success;
    }
    
    /**
     * Returns a MessageDigest if one of the tested varieties is found for this
     * JVM. If none of the test digests are available, this returns null.
     * @return a MessageDigest or null
     * @throws NoSuchAlgorithmException 
     */
    public MessageDigest getAvailableDigest() throws NoSuchAlgorithmException
    {
        if(isMDAvailable("SHA-1")==true)
            return MessageDigest.getInstance("SHA-1");
        else if(isMDAvailable("MD5")==true)
            return MessageDigest.getInstance("MD5");
        else if(isMDAvailable("SHA-256")==true)
            return MessageDigest.getInstance("SHA-256");
        return null;
    }
}