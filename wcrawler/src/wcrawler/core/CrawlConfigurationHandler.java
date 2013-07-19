
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 3:08:48 PM  Jul 16, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler.core;

import wcrawler.information.CrawlConfiguration;


public class CrawlConfigurationHandler {

    // load crawl configuration from file config.xml
    public CrawlConfiguration loadCrawlConfigFromXml(){
        return new CrawlConfiguration();
    }
    
    // update crawl config file
    public boolean updateCrawlConfigXml(){
        return true;
    }
}
