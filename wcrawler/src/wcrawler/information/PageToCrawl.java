/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:25:14 AM Jul 12, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.information;

public class PageToCrawl extends Page {

    private boolean isRetry;
    private boolean isRoot;

    public boolean isIsRetry() {
        return isRetry;
    }

    public void setIsRetry(boolean isRetry) {
        this.isRetry = isRetry;
    }

    public boolean isIsRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }
}
