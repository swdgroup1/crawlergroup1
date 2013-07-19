
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 10:24:14 AM  Jul 12, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler._interface;

import java.util.List;
import wcrawler.information.PageToCrawl;


public interface IScheduler {
    // gets next page from working queue to crawl
    PageToCrawl getNextPageToCrawl();
 
    // adds pages to working queue
    void addPagesToCrawl(List<PageToCrawl> page);
    
    // clears all pages in working queue
    void clear();
    
    // number of pages has been parsed and added to queue
    long getNumberOfPageToCrawl();
    
    // number of page has been crawled
    long getNumberOfCrawledPage();
        
}
