/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:20:01 AM Jul 15, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import wcrawler._interface.ICrawlDecisionMaker;
import wcrawler._interface.IHyperLinkParser;
import wcrawler._interface.IPageRequester;
import wcrawler._interface.IScheduler;
import wcrawler.information.CrawlConfiguration;
import wcrawler.information.CrawlContext;
import wcrawler.information.CrawlDecision;
import wcrawler.information.CrawledPage;
import wcrawler.information.PageToCrawl;

public class Crawler implements Runnable {

    private IPageRequester pageRequester;
    private IScheduler scheduler;
    private IHyperLinkParser hyperLinkParser;
    private ICrawlDecisionMaker crawlDecisionMaker;
    private CrawlConfiguration crawlConfiguration;
    private MultiThreadManager threadManager;
    private CrawlContext crawlContext;
    private static Logger _logger = Logger.getLogger(Crawler.class.getName());
    private boolean isCompleted;

    public Crawler() {
    }

    public Crawler(IPageRequester pageRequester, IScheduler scheduler, IHyperLinkParser hyperLinkParser, 
            ICrawlDecisionMaker crawlDecisionMaker, CrawlConfiguration crawlConfiguration, 
            MultiThreadManager threadManager) {
        this.pageRequester = pageRequester;
        this.scheduler = scheduler;
        this.hyperLinkParser = hyperLinkParser;
        this.crawlDecisionMaker = crawlDecisionMaker;
        this.crawlConfiguration = crawlConfiguration;

        isCompleted = false;
        this.threadManager = threadManager;

        crawlContext = new CrawlContext();
    }

    /**
     * This method will be invoked to run crawler,
     */
    private void crawl() {
        int robotstxtCrawlDelayInSecs = 0;
        int crawlDealayConfigInSecs = crawlConfiguration.getPolitenessDelay();

        if (crawlConfiguration.isPolitenessPolicyEnable()) {
            // Processing politeness policy
            respectPolitenessPolicyHandler();
        }

        //PageToCrawl page = scheduler.getNextPageToCrawl();

        //_logger.info("About to crawl site: " + page.getAbsoluteUrl());

        // This loops will create a number of threads to do "processPage", 1 thread = 1 processPage
        while (!isCompleted) {
            if (scheduler.getNumberOfPageToCrawl() > 0) {
                // Create runnable 
                System.out.println("create processPage");
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        PageToCrawl page = scheduler.getNextPageToCrawl();
                        System.out.println("Page to crawl: " + page.getAbsoluteUrl());
                        processPage(page);
                    }
                };
                threadManager.addTask(task);
            } else {
                isCompleted = false;
            }

            if (threadManager.isExecutorTerminated()) {
                isCompleted = false;
            }
        }
    }

    /**
     * reads information from robots.txt to set up rate limiter based on
     * crawl-delay and set up list of directory which is not allowed
     */
    private void respectPolitenessPolicyHandler() {
    }

    /**
     * This method represents for 1 thread which will crawl a page, then
     * schedule links in that page
     *
     * @param page: page to crawl
     */
    private void processPage(PageToCrawl page) {
            if (page == null) {
                return;
            }

        if (!allowToCrawlPage(page, crawlContext)) {
            return;
        }

            CrawledPage crawledPage = crawlPage(page);

            if (crawledPage != null) {
                if (allowToCrawlPageLinks(crawledPage, crawlContext)) {
                    scheduleUrls(crawledPage);
                }
            }

    }

    /**
     * crawls a page then return a crawled page
     *
     * @param page: page to crawl
     * @return CrawledPage
     */
    private CrawledPage crawlPage(PageToCrawl page) {
        _logger.debug("About to crawl page " + page.getAbsoluteUrl());

        //implements ratelimiter here first

        CrawledPage crawledPage = pageRequester.fetchPage(page, crawlConfiguration);

        if (crawledPage == null) {
            _logger.debug("crawledPage is null " + page.getAbsoluteUrl());
        } else {
            _logger.info("Crawl page " + page.getAbsoluteUrl() + " completed, Status: " + crawledPage.getResponseCode() + " " + crawledPage.getResponseMessage());
        }

        return crawledPage;
    }

    /**
     * makes decision about crawling a page (allow or not)
     *
     * @param: page decision to crawl this page
     * @param: crawlContext context to make decision
     */
    private boolean allowToCrawlPage(PageToCrawl page, CrawlContext crawlContext) {
        CrawlDecision crawlDecision = crawlDecisionMaker.crawlPageDecision(page, crawlContext);

        if (crawlDecision.isAllow()) {
            addPageToContext(page);
        } else {
            _logger.debug("Page " + page.getAbsoluteUrl() + " is not crawled, " + crawlDecision.getReason());
        }

        return crawlDecision.isAllow();
    }

    /**
     * makes decision about crawling links in a page
     *
     * @param: page decision to crawl links in this page
     * @param: crawlContext context to make decision
     */
    private boolean allowToCrawlPageLinks(CrawledPage page, CrawlContext crawlContext) {
        CrawlDecision crawlDecision = crawlDecisionMaker.crawlPageLinksDecision(page, crawlContext);

        if (!crawlDecision.isAllow()) {
            _logger.debug("Links on page " + page.getAbsoluteUrl() + " is not crawled, " + crawlDecision.getReason());
        }

        return crawlDecision.isAllow();
    }

    /**
     * After deciding to crawl the page, add the page to current context
     *
     * @param: page page to crawl
     */
    private void addPageToContext(PageToCrawl page) {
        crawlContext.setCrawledUrls(page.getAbsoluteUrl());
    }

    /**
     * After crawling page, start scraping content of the crawled page
     *
     * @param : page crawled page
     */
    private void addScrapedPage(CrawledPage page) {
        crawlContext.setScrapedUrls(page.getAbsoluteUrl());
    }

    /**
     * Schedule the urls from crawled page, add them to queue
     *
     * @param: page CrawledPage
     */
    private void scheduleUrls(CrawledPage page) {
        System.out.println("Scheduler page " + page.getAbsoluteUrl());
        List<String> urls = hyperLinkParser.getUrls(page, null);

        //List<PageToCrawl> pageToSchedule = new ArrayList<>();

        if (urls != null) {
            for (String url : urls) {
                if (url == null || url.trim().length() == 0) {
                    continue;
                }
                try {
                    PageToCrawl pageToCrawl = new PageToCrawl();
                    URL webUrl = new URL(url);
                    pageToCrawl.setAbsoluteUrl(url);
                    pageToCrawl.setIsRoot(false);
                    pageToCrawl.setIsRetry(false);
                    pageToCrawl.setUrl(webUrl);             

                    //pageToSchedule.add(pageToCrawl);
                    if (allowToCrawlPage(pageToCrawl, crawlContext)) {
                        System.out.println("add to queue "+url);
                        scheduler.addPage(pageToCrawl);
                        crawlContext.addQueuedPage(url);
                    }

                } catch (MalformedURLException ex) {
                    java.util.logging.Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //scheduler.addPagesToCrawl(pageToSchedule);
        }
    }

    @Override
    public void run() {
        crawl();
    }
}
