
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 10:31:16 AM  Jul 12, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler.information;


public class CrawlDecision {
    // Allow to crawl or not 
    private boolean allow;
    
    // Reason why dont allow to crawl
    private String reason;

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
}
