
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 10:25:25 AM  Jul 12, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler.information;

import java.net.URL;

public class Page {
    private String contentType;
    private String contentEncoding;
    private String contentCharset;
    
    private URL url;
    private String absoluteUrl;

    public String getAbsoluteUrl() {
        return absoluteUrl;
    }

    public void setAbsoluteUrl(String absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }
    
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getContentCharset() {
        return contentCharset;
    }

    public void setContentCharset(String contentCharset) {
        this.contentCharset = contentCharset;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
    
}
