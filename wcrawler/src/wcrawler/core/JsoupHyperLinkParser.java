
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 10:31:58 AM  Jul 12, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler.core;

import java.util.List;
import wcrawler._interface.IHyperLinkParser;
import wcrawler.information.CrawledPage;


public class JsoupHyperLinkParser implements IHyperLinkParser{

    // Parse hyperlink from raw html 
    @Override
    public List<String> getUrls(CrawledPage page) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
