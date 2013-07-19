
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 10:32:20 AM  Jul 12, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler.core;

import java.util.List;
import wcrawler._interface.IScheduler;
import wcrawler.information.PageToCrawl;

public class FIFOScheduler implements IScheduler{

    @Override
    public PageToCrawl getNextPageToCrawl() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addPagesToCrawl(List<PageToCrawl> page) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getNumberOfPageToCrawl() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getNumberOfCrawledPage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
