/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wcrawler.core;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import wcrawler.information.CrawlConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Connect MySQL database
 * @author Vo Anh
 */
public class DatabaseConnect {
    private CrawlConfiguration crawlConfiguration;
    private CrawlConfigurationHandler crawlConfigurationHandler ;
    //
    public boolean  isConnectDatabase(){
        crawlConfigurationHandler = new CrawlConfigurationHandler();
        crawlConfiguration = crawlConfigurationHandler.loadCrawlConfigFromXml();
        //Create connection connect database
        Connection connection = null;
        //Get value for variable connect database
        String driver = crawlConfiguration.getDriver();
        String url = crawlConfiguration.getUrl();
        String username = crawlConfiguration.getUsername();
        String password = crawlConfiguration.getPassword();
        String database = crawlConfiguration.getDatabase();
        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(url+ database, username, password); //Get Connnect
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
        return false;
    }  
}
