
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 10:23:51 AM  Jul 12, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler._interface;

import java.util.List;
import wcrawler.information.CrawledPage;

public interface IHyperLinkParser {
    List<String> getUrls(CrawledPage page);    
}
