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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
import static wcrawler.crawler.CrawlCreator._logger;
import wcrawler.information.CrawlConfiguration;
import wcrawler.information.CrawlFilterPattern;
import wcrawler.information.CrawledPage;
import wcrawler.information.PageToCrawl;
import wcrawler.robotstxt.Robotstxt;
import wcrawler.robotstxt.RobotstxtLoader;
import wcrawler.robotstxt.RobotstxtRecord;

public class CrawlCreator {

    private IPageRequester pageRequester;
    private IScheduler scheduler;
    private IHyperLinkParser hyperLinkParser;
    private ICrawlDecisionMaker crawlDecisionMaker;
    private CrawlConfiguration crawlConfiguration;
    private MultiThreadManager threadManager;
    private List<String> robotstxtAllow;
    private List<String> robotstxtDisallow;
    private List<String> containLinkPattern;
    private List<String> containInformationPattern;
    private List<String> filterAllow;
    private List<String> filterDisallow;
    static Logger _logger = Logger.getLogger(CrawlCreator.class);

    public CrawlCreator(CrawlConfiguration crawlConfiguration, CrawlFilterPattern crawlFilterPattern) {
        this.threadManager = new MultiThreadManager(crawlConfiguration.getMaxConcurrentThread());
        this.hyperLinkParser = new JsoupHyperLinkParser();
//        this.crawlDecisionMaker = new CrawlDecisionMaker(null, null, null, null, null, null);
        this.crawlConfiguration = crawlConfiguration;
    }

    /**
     * Adds a new seed URL. A seed URL is a URL that is fetched by the crawler
     * to extract new URLs in it and follow them for crawling.
     *
     * @param pageUrl the URL of the seed
     */
    public void addSeed(String url) {

        // Check robotstxt about this directory, allow or not
        scheduler = new FIFOScheduler();
        pageRequester = new PageRequester();
        PageToCrawl page = new PageToCrawl();

        try {
            URL _url = new URL(url);
            page.setAbsoluteUrl(url);
            page.setIsRoot(true);
            page.setUrl(_url);

            if (crawlConfiguration.isPolitenessPolicyEnable()) {
                // Processing politeness policy
                respectPolitenessPolicyHandler(url);
            }

            getFilterPattern();

            crawlDecisionMaker = new CrawlDecisionMaker(robotstxtAllow, robotstxtDisallow, containLinkPattern, containInformationPattern, filterAllow, filterDisallow);
            // add to queue
            scheduler.addPage(page);
            Crawler crawler = new Crawler(pageRequester, scheduler, hyperLinkParser, crawlDecisionMaker, crawlConfiguration);
            threadManager.addTask(crawler);


            _logger.info("Add Crawler to list ");
        } catch (MalformedURLException ex) {
            _logger.error("Problem with setting URL", ex);
        }
    }

    /**
     * reads information from robots.txt to set up rate limiter based on
     * crawl-delay and set up list of directory which is not allowed
     */
    private void respectPolitenessPolicyHandler(String url) throws MalformedURLException {
        PageToCrawl page = new PageToCrawl();
        URL _url = new URL(url);
        page.setAbsoluteUrl(url + "/robots.txt");
        page.setIsRoot(true);

        CrawledPage crawledPage = pageRequester.fetchPage(page, crawlConfiguration);

        RobotstxtLoader robotstxtLoader = new RobotstxtLoader();
        Robotstxt robotstxt = robotstxtLoader.loadRobotstxt(crawledPage.getRawContent());
        if (robotstxt != null) {
            RobotstxtRecord robotstxtRecord = robotstxt.getRecord("*");
            if (robotstxtRecord != null) {
                robotstxtAllow = robotstxtRecord.getAllow();
                robotstxtDisallow = robotstxtRecord.getDisallows();
            }
        }
    }
    final static Charset ENCODING = StandardCharsets.UTF_8;

    private void getFilterPattern() {
        try {

            // Contain Link Pattern
            containLinkPattern = readTextFile("linkpattern.txt");
            // Contain Information Pattern
            containInformationPattern = readTextFile("infopattern.txt");
            // FilterAllow
            filterAllow = readTextFile("allowpattern.txt");
            // Filter Disallow
            filterDisallow = readTextFile("disallowpattern.txt");

        } catch (IOException ex) {
            _logger.error(ex.getMessage());
        }

    }

    private List<String> readTextFile(String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        return Files.readAllLines(path, ENCODING);
    }

    public void start() {
        threadManager.start();
    }

    public void pause() {
        threadManager.pause();
    }

    public void resume() {
        threadManager.resume();
    }

    public void stop() {
        threadManager.stop();
    }

    public static void main(String[] args) {
//        CrawlConfigurationHandler crawlConfigurationHandler = new CrawlConfigurationHandler();
//        CrawlConfiguration crawlConfig = crawlConfigurationHandler.loadCrawlConfigFromXml();
//
//        CrawlCreator crawlCreator = new CrawlCreator(crawlConfig);
//        crawlCreator.addSeed("http://www.drugs.com");
//        crawlCreator.addSeed("http://www.stackoverflow.com");
//
//        // Start thread manager
//        crawlCreator.threadManager.start();
    }
}
