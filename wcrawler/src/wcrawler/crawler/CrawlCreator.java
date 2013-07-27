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
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import wcrawler._interface.ICrawlDecisionMaker;
import wcrawler._interface.IHyperLinkParser;
import wcrawler._interface.IPageRequester;
import wcrawler._interface.IScheduler;
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
import wcrawler._interface.IScraper;
import wcrawler.core.DatabaseConnect;
import wcrawler.core.Scrapper;

public class CrawlCreator {

    private IHyperLinkParser hyperLinkParser;
    private CrawlConfiguration crawlConfiguration;
    private MultiThreadManager threadManager;
    private List<String> robotstxtAllow;
    private List<String> robotstxtDisallow;
    private List<String> containLinkPattern;
    private List<String> containInformationPattern;
    CrawlFilterPattern crawlFilterPattern;
    static Logger _logger = Logger.getLogger(CrawlCreator.class);

    public CrawlCreator(CrawlConfiguration crawlConfiguration, CrawlFilterPattern crawlFilterPattern) {
        this.threadManager = new MultiThreadManager(crawlConfiguration.getMaxConcurrentThread());
        this.hyperLinkParser = new JsoupHyperLinkParser();
//        this.crawlDecisionMaker = new CrawlDecisionMaker(null, null, null, null, null, null);
        this.crawlConfiguration = crawlConfiguration;
        this.crawlFilterPattern = crawlFilterPattern;
    }

    /**
     * Adds a new seed URL. A seed URL is a URL that is fetched by the crawler
     * to extract new URLs in it and follow them for crawling.
     *
     * @param pageUrl the URL of the seed
     */
    public void addSeed(String url) {

        // Check robotstxt about this directory, allow or not
        IScheduler scheduler = new FIFOScheduler();
        IPageRequester pageRequester = new PageRequester();
        IScraper scraper = new Scrapper(new DatabaseConnect(crawlConfiguration));
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

            ICrawlDecisionMaker crawlDecisionMaker = new CrawlDecisionMaker(robotstxtAllow, robotstxtDisallow, 
                    containLinkPattern, containInformationPattern, 
                    crawlFilterPattern.getAllows(), crawlFilterPattern.getDisallows());
            // add to queue
            scheduler.addPage(page);
            Crawler crawler = new Crawler(pageRequester, scheduler, hyperLinkParser, 
                    crawlDecisionMaker, crawlConfiguration,threadManager,scraper);
            //threadManager.addTask(crawler);
            crawler.run();


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

        CrawledPage crawledPage = new PageRequester().fetchPage(page, crawlConfiguration);

        RobotstxtLoader robotstxtLoader = new RobotstxtLoader();
        Robotstxt robotstxt = robotstxtLoader.loadRobotstxt(crawledPage.getRawContent());
        if (robotstxt != null) {
            RobotstxtRecord robotstxtRecord = robotstxt.getRecord("*");
            if (robotstxtRecord != null) {
                robotstxtAllow = new ArrayList<>();
                for(String allow:robotstxtRecord.getAllow()){
                    robotstxtAllow.add(url+allow);
                }
                robotstxtDisallow = new ArrayList<>();
                for(String disallow:robotstxtRecord.getDisallows()){
                    robotstxtDisallow.add(url+disallow);
                }
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
        
        //Test insert data into database
//        DatabaseConnect databaseConnect = new DatabaseConnect();
//        boolean flag = databaseConnect.isInsertDatabase("http://www.drugs.com/", "Welcome to Drugs.com", "Drugs.com is the most popular, comprehensive and up-to-date source of drug information online. Providing free, peer-reviewed, accurate and independent data on more than 24,000 prescription drugs, over-the-counter medicines & natural products. Find helpful tools, wallet size personal medication records, mobile applications and more","http://www.drugs.com/alpha/d1.html");
//        if (flag) {
//            System.out.println("Insert OK");
//        }
    }
}
