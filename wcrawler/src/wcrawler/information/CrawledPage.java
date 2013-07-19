/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:25:48 AM Jul 12, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.information;

public class CrawledPage extends Page {
    // rawContent is the html which is downloaded directly from page

    private String rawContent;
    // data from page in byte to use in case it is a binary stream
    private byte[] contentData;
    // size of a page in byte
    private long pageSizeInBytes;
    
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
    
    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }

    public byte[] getContentData() {
        return contentData;
    }

    public void setContentData(byte[] contentData) {
        this.contentData = contentData;
    }

    public long getPageSizeInBytes() {
        return pageSizeInBytes;
    }

    public void setPageSizeInBytes(long pageSizeInBytes) {
        this.pageSizeInBytes = pageSizeInBytes;
    }
}
