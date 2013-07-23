/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:20:20 AM Jul 15, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.crawler;

// This class handles creating "Crawler"
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Logger;
import wcrawler._interface.ICrawlDecisionMaker;
import wcrawler._interface.IHyperLinkParser;
import wcrawler._interface.IPageRequester;
import wcrawler._interface.IScheduler;
import wcrawler.core.CrawlDecisionMaker;
import wcrawler.core.FIFOScheduler;
import wcrawler.core.JsoupHyperLinkParser;
import wcrawler.core.PageRequester;
import wcrawler.information.CrawlConfiguration;
import wcrawler.information.PageToCrawl;

public class CrawlCreator {

    private IPageRequester pageRequester;
    private IScheduler scheduler;
    private IHyperLinkParser hyperLinkParser;
    private ICrawlDecisionMaker crawlDecisionMaker;
    private CrawlConfiguration crawlConfiguration;
    private MultiThreadManager threadManager;
    static Logger _logger = Logger.getLogger(CrawlCreator.class);

    public CrawlCreator(CrawlConfiguration crawlConfiguration) {
        this.pageRequester = new PageRequester();
        this.scheduler = new FIFOScheduler();
        this.hyperLinkParser = new JsoupHyperLinkParser();
        this.crawlDecisionMaker = new CrawlDecisionMaker();
        this.crawlConfiguration = crawlConfiguration;
        this.threadManager = new MultiThreadManager(crawlConfiguration.getMaxConcurrentThread());
    }

    /**
     * Adds a new seed URL. A seed URL is a URL that is fetched by the crawler
     * to extract new URLs in it and follow them for crawling.
     *
     * @param pageUrl the URL of the seed
     */
    public void addSeed(String url) {
        PageToCrawl page = new PageToCrawl();
        try {
            URL _url = new URL(url);
            page.setAbsoluteUrl(url);
            page.setIsRoot(true);
            page.setUrl(_url);
            scheduler.addPage(page);
        } catch (MalformedURLException ex) {
            _logger.error("Problem with setting URL", ex);
        }
    }

    /**
     * Creates crawlers and then adds them to executor list
     *
     * @param numberOfCrawlers number of crawlers wants to create
     */
    public void createCrawler(int numberOfCrawlers) {

        for (int i = 1; i <= numberOfCrawlers; i++) {
            Crawler crawler = new Crawler(pageRequester, scheduler, hyperLinkParser, crawlDecisionMaker, crawlConfiguration);
            threadManager.addTask(crawler);
            _logger.info("Add Crawler " + i + " to list ");
        }

        // Start thread manager
        threadManager.start();
    }
}
