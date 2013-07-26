
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 8:44:53 PM  Jul 26, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler._interface;

import wcrawler.information.CrawledPage;


public interface IScraper {
    void getInnerText(CrawledPage page);
}
