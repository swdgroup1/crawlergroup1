
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 10:23:12 AM  Jul 12, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler._interface;

import wcrawler.information.CrawlConfiguration;
import wcrawler.information.CrawlContext;
import wcrawler.information.CrawledPage;
import wcrawler.information.PageToCrawl;


public interface IPageRequester {
    
    // makes a request 
    CrawledPage fetchPage(PageToCrawl page, CrawlConfiguration config);
    
}
