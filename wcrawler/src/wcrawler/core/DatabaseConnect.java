/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wcrawler.core;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import wcrawler.information.CrawlConfiguration;
/**
 * Connect MySQL database
 * @author Vo Anh
 */
public class DatabaseConnect {
    //Use for read file config
    private CrawlConfiguration crawlConfiguration;
    static Logger _logger = Logger.getLogger(DatabaseConnect.class); 
    

    public DatabaseConnect(CrawlConfiguration crawlConfiguration) {
        this.crawlConfiguration = crawlConfiguration;
    }  
    
    
    /*
     * Insert data into database 
     * 4 parameter : url, drugName, drugInfo, rawHtml
     */
    public boolean  isInsertDatabase(String url, String drugName, String drugInfo, String rawHtml){
        //Get value for variable connect database
        String driver = crawlConfiguration.getDriver();
        String urlDatabase = crawlConfiguration.getUrl();
        String username = crawlConfiguration.getUsername();
        String password = crawlConfiguration.getPassword();
        String database = crawlConfiguration.getDatabase();
        Connection connection = null;;
        try {
            Class.forName(driver).newInstance();
            //Get Connection
            connection = DriverManager.getConnection(urlDatabase+ database, username, password); 
            //sql statment for insert data
            
            String sql="INSERT INTO Test(URL,Drug_Name,Drug_Infomation,Raw_Html) VALUES('"+url+"','"+drugName+"','"+drugInfo+"','"+rawHtml +"')";
            Statement statement = connection.createStatement();
            
            _logger.debug(sql);
            //excute update sql statement
            statement.executeUpdate(sql);
            //return true if insert succesfully
           return true;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
//            Logger.getLogger(CrawlConfigurationHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
//            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }finally
        {
            try {
                connection.close();
            } catch (SQLException ex) {
//                Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //fail to insert
        return false;
    }
}
