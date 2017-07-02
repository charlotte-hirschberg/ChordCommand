/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chordcommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author Charlotte
 */

public class ChordUtil 
{
    private PreparedStatement psMajKey;
    private PreparedStatement psChordData;
    /**
     * Constructor prepares a series of PreparedStatements to be used repeatedly
     * while building Chord and Scale objects
     * TO DO: Close resources
     * @throws java.sql.SQLException
     */
    public ChordUtil() throws SQLException
    {
        BasicDataSource source = DBUtil.getDataSource();
        Connection conn = source.getConnection();
        psMajKey = conn.prepareStatement("SELECT MajorSuffixes FROM MajorKeyLU WHERE MajorKeyName = ?");
        psChordData = conn.prepareStatement("SELECT ChordStruc, ChordSuffixes "
                + "FROM SymbolConversion as sc JOIN ChordStructure as cst ON sc.ChordStructID = cst.ChordStrucID "
                + "WHERE ChordID = ?");
    }
    /**
     * Convert text to given Form if provided. Otherwise, normalize with NFC.
     * Return true if text satisfies regex argument, false if it does not.
     * @param text  TextField entry
     * @param regex
     * @param form
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
    
    /**
     * Extract 0-2 characters that denote a key from the string, depending on what the user has entered.
     * @param entry
     * @return 
     */
    public String extractKey(String entry)
    {
        // Starts with A,B,C,D,E,F,or G
        if(Pattern.matches("^[A-G]", entry))
        {
            // Letter followed by flat symbol
            if(Pattern.matches("^[A-G][b♭]", entry))
               return entry.substring(0, 2);            // return 2 char
            else
                return String.valueOf(entry.charAt(0)); // return single letter
        }
        else
            return null;
    }
    
    /**
     * TO DO: Switch flat symbol to b in key string?
     * Retrieve a comma-separated MajorSuffixes string from database and use it
     * to build a new MajorKey.
     * @param key
     * @return
     * @throws SQLException 
     */
    public MajorKey buildKey(String key) throws SQLException
    {
        MajorKey mKey = new MajorKey(key);
        
        psMajKey.setString(1, String.valueOf(key.charAt(0)));
        ResultSet rs = psMajKey.executeQuery();
        
        if(rs != null)
            mKey.setSuffixes(rs.getString(1));
        
        return mKey;
    }
    
    /**
     * Convert user's entry to the accepted database format, with 'M' for major;
     * '-' for minor, 'b' for flat, '+' for augmented, '°' for diminished.
     * 
     * @param entry     user's entry with spaces and key stripped away
     * @return 
     */
    public String formatSymbol(String entry)
    {
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
    
    public Chord buildChord(String entry, MajorKey mKey) throws SQLException
    {
        Chord chord1;
        
        psChordData.setString(1, entry);
        ResultSet rs = psChordData.executeQuery();
        
        // If rs is null, SQLException occurs
        // Otherwise, execution proceeds
        chord1 = new Chord();
        chord1.setMajKey(mKey);
        chord1.setDBName(entry);
        chord1.setChordNums(rs.getString("ChordStruc"));
        chord1.setChordNumArr(rs.getString("ChordStruc"));
        chord1.setChordSuffsArr(rs.getString("ChordSuffixes"));
        chord1.setPitches();
        
        return chord1;
    }
}
