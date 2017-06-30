package chordcommand;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    
    protected static BasicDataSource getDataSource()
    {
        if(ds == null || ds.isClosed())
        {
            BasicDataSource source = new BasicDataSource();
            source.setDriverClassName(dbProps.getProperty("driverClass"));
            source.setUrl(dbProps.getProperty("dbPath"));
            source.setUsername(dbProps.getProperty("dbUser"));
            source.setPassword(dbProps.getProperty("dbPW"));
            ds = source;
        }
        return ds;
    }
    
    /**
     * Retrieve a set of records from db and fill an ObservableList with
     * values from 1 column in the ResultSet
     * @param query
     * @param colName   Name of column from which list values derive
     * @return
     * @throws SQLException 
     */
    protected ObservableList<String> toList(String query, String colName) throws SQLException
    {
        ObservableList<String> list = FXCollections.observableArrayList();
        try (BasicDataSource source = DBUtil.getDataSource(); 
		Connection connection = source.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query))
	{
            while(rs.next())
                list.add(rs.getString(colName));
	}
	catch (Exception e)
	{
            e.printStackTrace();
	}
        return list;
    }
}
