package chordcommand.util;

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
 * @Course: SDEV 435 ~ Applied Software Practice
 * @Author Name: Charlotte Hirschberger
 * @Assignment ChordCommand
 * @Date: Jun 12, 2017
 * @Description: The DBUtil class uses Apache's pooling and DBCP libraries to
 * create a connection pool. A limited number of connections remain open and are
 * recycled as necessary, instead of repeatedly establishing new connections, 
 * which would drain resources.
 */
public class DBUtil 
{
    private static BasicDataSource ds;
    private static Properties dbProps;
    private final static String FILE_NAME = "dbprops.properties";
    
    /**
     * Load the database-connection parameters from a .properties file.
     * @throws IOException 
     */
    public DBUtil() throws IOException
    {
        PropertiesUtil pUtil = new PropertiesUtil();
        dbProps = pUtil.loadParams(FILE_NAME);
    }
    
    /**
     * If a BasicDataSource hasn't already been created or is no longer open,
     * create a new one.
     * @return a BasicDataSource object
     */
    public static BasicDataSource getDataSource()
    {
        if(ds == null || ds.isClosed())
        {
            ds = new BasicDataSource();
            ds.setDriverClassName(dbProps.getProperty("driverClass"));
            ds.setUrl(dbProps.getProperty("dbPath"));
            ds.setUsername(dbProps.getProperty("dbUser"));
            ds.setPassword(dbProps.getProperty("dbPW"));
        }
        return ds;
    }
    
    /**
     * Retrieve a set of records from the database and fill an ObservableList 
     * with values from 1 column in the ResultSet
     * @param query a query
     * @param colName name of column from which list values derive
     * @return an ObservableList
     * @throws SQLException 
     */
    public ObservableList<String> toList(String query, String colName) throws SQLException
    {
        ObservableList<String> list = FXCollections.observableArrayList();

        ds = getDataSource();
        try (Connection connection = ds.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query))
	{
            while(rs.next())
                list.add(rs.getString(colName));
	}
        return list;
    }
    
    /**Method for use by developers to determine how many connections are open
     * and to verify that resources are being closed and connections are being 
     * pooled.
     */
    public void printDataSourceStats()
    {
        if(ds == null)
            System.out.println("The data source hasn't been instantiated.");
        else if (ds.isClosed())
            System.out.println("The data source is currently closed.");
        else
        {
            System.out.println("NumActive: " + ds.getNumActive());
            System.out.println("NumIdle: " + ds.getNumIdle());
        }
    }
    
    /**
     * Closes a DataSource
     * @throws SQLException 
     */
    public void closeDataSource() throws SQLException
    {
        if(ds != null)
        {
            ds.close();
        }
    }
} // End class DBUtil
