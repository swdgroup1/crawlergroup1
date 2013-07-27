/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wcrawler.core;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import wcrawler.information.CrawlConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Connect MySQL database
 * @author Vo Anh
 */
public class DatabaseConnect {
    //Use for read file config
    private CrawlConfiguration crawlConfiguration;
    private CrawlConfigurationHandler crawlConfigurationHandler ;
     // Connection connect database  
    Connection connection = null;
    // Statement execute sql
    Statement statement =null;
    /*
     * Insert data into database 
     * 4 parameter : url, drugName, drugInfo, rawHtml
     */
    public boolean  isInsertDatabase(String url, String drugName, String drugInfo, String rawHtml){
        
        crawlConfigurationHandler = new CrawlConfigurationHandler();
        crawlConfiguration = crawlConfigurationHandler.loadCrawlConfigFromXml();
        //Get value for variable connect database
        String driver = crawlConfiguration.getDriver();
        String urlDatabase = crawlConfiguration.getUrl();
        String username = crawlConfiguration.getUsername();
        String password = crawlConfiguration.getPassword();
        String database = crawlConfiguration.getDatabase();
        try {
            Class.forName(driver).newInstance();
            //Get Connection
            connection = DriverManager.getConnection(urlDatabase+ database, username, password); 
            //sql statment for insert data
            String sql="INSERT INTO Test(URL,Drug_Name,Drug_Infomation,Raw_Html) VALUES('"+url+"','"+drugName+"','"+drugInfo+"','"+rawHtml +"')";
            statement = connection.createStatement();
            //excute update sql statement
            statement.executeUpdate(sql);
            //return true if insert succesfully
           return true;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(CrawlConfigurationHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }finally
        {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //fail to insert
        return false;
    }
}
