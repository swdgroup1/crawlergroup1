/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:31:58 AM Jul 12, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import wcrawler._interface.IHyperLinkParser;
import wcrawler.information.CrawledPage;
import org.apache.log4j.Logger;

// Parse hyperlink from page using Jsoup
public class JsoupHyperLinkParser implements IHyperLinkParser {

    private static Logger _logger = Logger.getLogger(JsoupHyperLinkParser.class);
    
    /**
     * Parse hyperlink from raw html 
     * @param page : page to parse hyperlink
     * @return List<String> ; list of links
     */
    @Override
    public List<String> getUrls(CrawledPage page) {
        try {
            if(page == null){
                _logger.debug("CrawledPage is null, cannot get urls from it");
                return null;
            }
            
            String rawHtml = page.getRawContent();
            if (rawHtml.isEmpty()) {
                _logger.debug("Raw content of " + page.getAbsoluteUrl() + " is empty, cannot get urls from it");
                return null;
            }

            // Gets a jsoup Document from rawHtml
            Document doc = Jsoup.connect(rawHtml).get();

            // Selects all a[href] tag from rawHtml
            Elements urls = doc.select("a[href]");

            if (urls.isEmpty()) {
                _logger.debug("There is no url from this page: " + page.getAbsoluteUrl());
                return null;
            }

            // Add urls to list
            List<String> urlList = new ArrayList<String>();
            for (Element url : urls) {
                urlList.add(url.toString());
            }

            return urlList;
        } catch (IOException ex) {
            _logger.debug("Problem with Jsoup.connect(rawHtml).get: Line 37", ex);
        }

        return null;
    }
}
