package chordcommand;

import java.io.IOException;
import java.util.Properties;
import org.apache.commons.dbcp2.*;

/**
 *
 * @author Charlotte
 */
public class DBUtil 
{
    private static BasicDataSource ds;
    private static Properties dbProps;
    private final static String FILE_NAME = "dbprops.properties";
    
    public DBUtil() throws IOException
    {
        PropertiesUtil pUtil = new PropertiesUtil();
        dbProps = pUtil.loadParams(FILE_NAME);
    }
    
    private static BasicDataSource getDataSource()
    {
        if(ds == null)
        {
            ds = new BasicDataSource();
            ds.setUrl(dbProps.getProperty("dbPath"));
            ds.setUsername(dbProps.getProperty("dbUser"));
            ds.setPassword(dbProps.getProperty("dbPW"));
        }
        return ds;
    }
}
