package chordcommand.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbcp2.*;

/** 
 * Description: The DBUtil class uses Apache's pooling and DBCP libraries to
 * create a connection pool. A limited number of connections remain open and are
 * recycled as necessary, instead of repeatedly establishing new connections, 
 * which would drain resources.
 * <p>Course: SDEV 435 ~ Applied Software Practice</p>
 * <p>Author Name: Charlotte Hirschberger</p>
 * <p>Assignment ChordCommand</p>
 * Created Date: Jun 12, 2017
 */
public class DBUtil 
{
    private static BasicDataSource ds;
    private static Properties dbProps;
    private static boolean success; // database connection successfully established?
    
    /**
     * Create a new database utility with a specified set of database connection
     * parameters.
     * @param _dbProps  database connection parameters
     */
    public DBUtil(Properties _dbProps)
    {
        dbProps = _dbProps;
        success = false;
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
            success = true;
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
    
    /**
     * Returns _success member to indicate whether a connection has been
     * established during this session. For use on exit, to determine whether
     * attempting to fix a .chk file is warranted.
     * @return reference to success member
     */
    public boolean getSuccess()
    {
        return success;
    }
    
} // End class DBUtil
