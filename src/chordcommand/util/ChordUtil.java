package chordcommand.util;

import chordcommand.model.MajorKey;
import chordcommand.model.Scale;
import chordcommand.model.Chord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.dbcp2.BasicDataSource;

/** 
 * @Course: SDEV 435 ~ Applied Software Practice
 * @Author Name: Charlotte Hirschberger
 * @Assignment ChordCommand
 * @Date: Jun 12, 2017
 * @Description: This class contains the methods that validate and verify a
 * chord symbol entry and then interact with a database to retrieve data for
 * building Chord and Scale objects. Upon construction of the ChordUtil object,
 * prepared statements are created for use until the program terminates.
 */
public class ChordUtil 
{
    private PreparedStatement psMajKey;
    private PreparedStatement psChordData;
    private PreparedStatement psGetScales;
    
    /**
     * Constructor establishes a database connection and prepares a series of 
     * PreparedStatements to be used repeatedly while building Chord and Scale 
     * objects.
     * @throws java.sql.SQLException
     */
    public ChordUtil() throws SQLException
    {           
        BasicDataSource source = DBUtil.getDataSource();

        Connection conn = source.getConnection();
        psMajKey = conn.prepareStatement("SELECT MajorSuffixes FROM MajorKeyLU WHERE MajorKeyName = ?");
        psChordData = conn.prepareStatement("SELECT ChordID, ChordStruc, ChordSuffixes "
                    + "FROM SymbolConversion as sc JOIN ChordStructure as cst ON sc.ChordStructID = cst.ChordStrucID "
                    + "WHERE ChordSymbol = ?");
        psGetScales = conn.prepareStatement("SELECT ScaleName, WHForm, ScaleSuffixes, ScaleStruc "
                    + "FROM Scales as sc JOIN ScaleStructure as ss ON sc.ScaleStrucID = ss.ScaleStrucID "
                    + "JOIN ScalesChords as br ON sc.ScaleID = br.ScaleID "
                    + "JOIN SymbolConversion as sym ON br.ChordID = sym.ChordID WHERE sym.ChordID = ?;");
    }
    
    /**
     * Convert text to given Form if provided. Otherwise, normalize with NFC.
     * Return true if text satisfies regex argument, false if it does not.
     * @param text  TextField entry
     * @param regex regular expression
     * @param form  optional, a Normalizer form
     * @return      is valid input?
     */
    public boolean isValidInput(String text, String regex, Normalizer.Form form)
    {
        // Convert Unicode characters to a single format before validation        
        if(form == null) 
            text = Normalizer.normalize(text, Normalizer.Form.NFC);  
        else 
            text = Normalizer.normalize(text, form);
        
        Pattern rgx = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = rgx.matcher(text);
        
        return m.matches();
    }
    
    public String stripSpaces(String text)
    {
        text = text.replaceAll("\\s+","");
        return text;
    }
    
    /**
     * Retrieve a comma-separated MajorSuffixes string from database and use it
     * to build a new MajorKey.
     * @param key
     * @return a MajorKey object
     * @throws SQLException 
     */
    public MajorKey buildKey(String key) throws SQLException
    {
        MajorKey mKey = new MajorKey(key);
        
        psMajKey.setString(1, key);
        try(ResultSet rs = psMajKey.executeQuery())
        {
            if(rs.next())
            {
                mKey.setSuffixes(rs.getString("MajorSuffixes"));
            }
            else
                throw new SQLException();
        }
        return mKey;
    }
    
    /**
     * Convert user's entry to the accepted database format, with 'M' for major;
     * '-' for minor, 'b' for flat, '+' for augmented, '°' for diminished.
     * 
     * @param entry     user's entry with spaces and key stripped away
     * @return          the converted, database-friendly entry
     */
    public String formatSymbol(String entry)
    {
        entry = stripSpaces(entry);
        
        // Replace any instance of flat symbol with 'b'
        entry = entry.replace('♭', 'b');
        
        // Ignoring case, replace any instance of "aug" with '+'
        entry = entry.replaceAll("(?i)(aug)","+");
        
        // Ignoring case, replace any instance of "dim" with "°"
        entry = entry.replaceAll("(?i)(dim)","°");
        
        // Ignoring case, replace any instance of "mi" or "min" with "-"
        entry = entry.replaceAll("(?i)(mi)[n]?","-");
        
        // Ignoring case, replace any instance of "ma" or "ma" with "M"
        entry = entry.replaceAll("(?i)(ma)[j]?","M");
        
        // Replace any instance of delta symbol with 'M'
        entry = entry.replace('Δ', 'M');      
        
        // Replace any remaining 'm' with '-'
        entry = entry.replace('m','-');   
        
        // Capital M's not followed by 'a' or 'i' are left untouched
        
        return entry;
    }
    
    /**
     * Use the entry from formatSymbol to query the database for a matching
     * Chord record. If the query returns a record, use its fields to build a
     * Chord object.
     * @param entry the converted, database-friendly chord symbol
     * @param mKey a MajorKey object
     * @return  a Chord object
     * @throws SQLException 
     */
    public Chord buildChord(String entry, MajorKey mKey) throws SQLException
    {
        Chord chord1 = null;
        
        psChordData.setString(1, entry);
        try(ResultSet rs = psChordData.executeQuery())
        {
            if(rs.next())
            {
                chord1 = new Chord();
                chord1.setMajKey(mKey);
                chord1.setDBName(entry);
                chord1.setID(rs.getInt("ChordID"));
                chord1.setNumArr(rs.getString("ChordStruc"));
                chord1.setSuffsArr(rs.getString("ChordSuffixes"));
                chord1.setDisplayVals();
            }
            else
                throw new SQLException();
        }
        
        return chord1;
    }
    
    /**
     * Use the Chord ID to run a query that retrieves all related Scales from
     * the database. Construct a Scale object from each record and then add the
     * Scale object to the chord's list of Scale objects
     * @param chord
     * @throws SQLException 
     */
    public void buildScales(Chord chord) throws SQLException
    {
        psGetScales.setInt(1, chord.getId());
        try(ResultSet rs = psGetScales.executeQuery())
        {
            MajorKey mKey = chord.getMajKey();
            while(rs.next())
            {
                // Get the name, WH form, numbers, and suffixes
                String name = rs.getString("ScaleName");
                String wh = rs.getString("WHForm");
                String nums = rs.getString("ScaleStruc");
                String suffx = rs.getString("ScaleSuffixes");
                
                // Call Scale constructor
                Scale scale1 = new Scale(name, wh, mKey);
                
                // Calling MusicStructure methods inherited by Scale
                scale1.setNumArr(nums);
                scale1.setSuffsArr(suffx);
                scale1.setDisplayVals();
                
                // Add this scale to Chord's list
                chord.addScale(scale1);
            }
        }
    }
}// End class ChordUtil
