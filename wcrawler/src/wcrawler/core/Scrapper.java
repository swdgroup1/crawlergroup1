/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
package wcrawler.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import wcrawler._interface.IScraper;
import wcrawler.information.CrawledPage;
import wcrawler.core.DatabaseConnect;
/**
 *
 * @author Hai
 */
public class Scrapper implements IScraper {
    private DatabaseConnect databaseConnect;

    public Scrapper(DatabaseConnect databaseConnect) {
        this.databaseConnect = databaseConnect;
    }   
    
    
    @Override
    public void getInnerText(CrawledPage page) {
        Document doc = Jsoup.parse(page.getRawContent());

        Elements contentElement = doc.select("div.contentBox");
        Elements titleElement = doc.select("div.titleAudio clearAfter > h1");

        String drugsInfo = contentElement.text();

        String title = titleElement.text();
        
        //databaseConnect.isInsertDatabase(page.getAbsoluteUrl(), title, drugsInfo, page.getRawContent());
    }
}
