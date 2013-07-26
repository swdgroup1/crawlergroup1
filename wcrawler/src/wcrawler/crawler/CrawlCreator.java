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
import java.util.concurrent.ConcurrentSkipListSet;
import org.apache.log4j.Logger;
import wcrawler._interface.ICrawlDecisionMaker;
import wcrawler._interface.IHyperLinkParser;
import wcrawler._interface.IPageRequester;
import wcrawler._interface.IScheduler;
import wcrawler.core.CrawlConfigurationHandler;
import wcrawler.core.CrawlDecisionMaker;
import wcrawler.core.FIFOScheduler;
import wcrawler.core.JsoupHyperLinkParser;
import wcrawler.core.PageRequester;
import wcrawler.information.CrawlConfiguration;
import wcrawler.information.PageToCrawl;
import wcrawler.information.CrawlFilterPattern;
import wcrawler.robotstxt.*;

public class CrawlCreator {

    private IPageRequester pageRequester;
    private IScheduler scheduler;
    private IHyperLinkParser hyperLinkParser;
    private ICrawlDecisionMaker crawlDecisionMaker;
    private CrawlConfiguration crawlConfiguration;
    private MultiThreadManager threadManager;
    static Logger _logger = Logger.getLogger(CrawlCreator.class);

    public CrawlCreator(CrawlConfiguration crawlConfiguration,CrawlFilterPattern crawlFilterPattern,String domain) {
        this.pageRequester = new PageRequester();
        this.scheduler = new FIFOScheduler();
        this.hyperLinkParser = new JsoupHyperLinkParser();
        
        /**
         * Get information from robots.txt
         */
        try{
            
            RobotstxtLoader robotstxtLoader = new RobotstxtLoader();
        }catch(Exception e){
            e.printStackTrace();
        }
                      
        /**
         * Add dummy containLinkPattern and contain InformaitonPattern
         */
        ConcurrentSkipListSet<String> containLinkPattern = new ConcurrentSkipListSet<>();
        ConcurrentSkipListSet<String> containInformationPattern = new ConcurrentSkipListSet<>();
        containLinkPattern.add("http://");
        containInformationPattern.add("http://");
        
        this.crawlDecisionMaker = new CrawlDecisionMaker(null, null, containLinkPattern, 
                containInformationPattern, crawlFilterPattern.getAllows(), crawlFilterPattern.getDisallows());
        
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

        // Check robotstxt about this directory, allow or not

        PageToCrawl page = new PageToCrawl();
        try {
            URL _url = new URL(url);
            page.setAbsoluteUrl(url);
            page.setIsRoot(true);
            page.setUrl(_url);

            // add to queue
            
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
            Crawler crawler = new Crawler(pageRequester, new FIFOScheduler(), hyperLinkParser, crawlDecisionMaker, crawlConfiguration,threadManager);
            threadManager.addTask(crawler);
            _logger.info("Add Crawler " + i + " to list ");
        }

        // Start thread manager
        threadManager.start();
    }
    
    public static void main(String[] args){
//        CrawlConfigurationHandler crawlConfigurationHandler = new CrawlConfigurationHandler();
//        CrawlConfiguration crawlConfig = crawlConfigurationHandler.loadCrawlConfigFromXml();
//        
//        CrawlCreator crawlCreator = new CrawlCreator(crawlConfig);
//        crawlCreator.addSeed("http://www.drugs.com");
//        crawlCreator.addSeed("http://www.stackoverflow.com");
//        crawlCreator.addSeed("http://www.drugs.com/mtm");
//        
//        crawlCreator.createCrawler(1);
    }
}
