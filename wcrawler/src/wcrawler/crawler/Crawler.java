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
import wcrawler._interface.IScraper;

public class Crawler implements Runnable {

    private IPageRequester pageRequester;
    private IScheduler scheduler;
    private IHyperLinkParser hyperLinkParser;
    private ICrawlDecisionMaker crawlDecisionMaker;
    private CrawlConfiguration crawlConfiguration;
    private MultiThreadManager threadManager;
    private IScraper scrapper;
    private CrawlContext crawlContext;
    private static Logger _logger = Logger.getLogger(Crawler.class.getName());
    private boolean isCompleted;

    public Crawler() {
    }

    public Crawler(IPageRequester pageRequester, IScheduler scheduler, IHyperLinkParser hyperLinkParser,
            ICrawlDecisionMaker crawlDecisionMaker, CrawlConfiguration crawlConfiguration,
            MultiThreadManager threadManager,IScraper scrapper) {
        this.pageRequester = pageRequester;
        this.scheduler = scheduler;
        this.hyperLinkParser = hyperLinkParser;
        this.crawlDecisionMaker = crawlDecisionMaker;
        this.crawlConfiguration = crawlConfiguration;
        this.scrapper = scrapper;
        
        isCompleted = false;
        this.threadManager = threadManager;

        crawlContext = new CrawlContext();
    }

    /**
     * This method will be invoked to run crawler,
     */
    private void crawl() {

        // This loops will create a number of threads to do "processPage", 1 thread = 1 processPage
        if (!isCompleted) {
            if (scheduler.getNumberOfPageToCrawl() > 0) {
                // Create runnable 
                Runnable task = new Runnable() {
                    @Override
                    public void run() {

                        PageToCrawl page = scheduler.getNextPageToCrawl();
                        _logger.debug("Page to crawl: " + page.getAbsoluteUrl());
                        long before = System.currentTimeMillis();
                        try {
                            processPage(page);
                        } catch (InterruptedException ex) {
                            _logger.error(ex.getMessage());
                        }
                        long after = System.currentTimeMillis();

                        _logger.debug("process page: " + page.getAbsoluteUrl() + " took: " + (after - before) + " ms");
                    }
                };

                threadManager.addTask(task);
                _logger.debug("add task");
            } else {
                _logger.info("There are no pages to crawl in queue");
                isCompleted = true;
            }

            if (threadManager.isExecutorTerminated()) {
                _logger.info("Threads are terminated");
                isCompleted = true;
            }
        }
    }

    /**
     * This method represents for 1 thread which will crawl a page, then
     * schedule links in that page
     *
     * @param page: page to crawl
     */
    private void processPage(PageToCrawl page) throws InterruptedException {
        _logger.debug("Enter process Page");
        if (page == null) {
            return;
        }

//        if (!allowToCrawlPage(page, crawlContext)) {
//            crawl();
//            return;
//        }

        CrawledPage crawledPage = crawlPage(page);

        boolean isCrawled = false;
        if (scheduler.getNumberOfPageToCrawl() > 0) {
            _logger.debug("start crawl after " + page.getAbsoluteUrl() + ", number of pages to crawl: " + scheduler.getNumberOfPageToCrawl());
            crawl();
            isCrawled = true;
        }

        if (crawledPage == null) {
            _logger.debug("crawledPage is null " + page.getAbsoluteUrl());
        } else {
            _logger.info("Crawl page " + page.getAbsoluteUrl() + " completed, Status: " + crawledPage.getResponseCode() + " " + crawledPage.getResponseMessage());
            crawledPage.setAbsoluteUrl(page.getAbsoluteUrl());

            if (allowToCrawlPageLinks(crawledPage, crawlContext)) {
                _logger.debug("Enter scheduler!");
                long before = System.currentTimeMillis();
                scheduleUrls(crawledPage);
                long after = System.currentTimeMillis();

                _logger.debug("Schedule took: " + (after - before) + " ms");
            }

            if (allowToCrawlInformation(crawledPage, crawlContext)) {
                _logger.debug("Enter scrapper");
                long before = System.currentTimeMillis();
                scrapInformation(crawledPage);
                long after = System.currentTimeMillis();

                _logger.debug("Scrap took: " + (after - before) + " ms");
            }
        }

        if (!isCrawled) {
            _logger.debug("start crawl after " + page.getAbsoluteUrl() + ", number of pages to crawl: " + scheduler.getNumberOfPageToCrawl());
            crawl();
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
        long before = System.currentTimeMillis();
        CrawledPage crawledPage = pageRequester.fetchPage(page, crawlConfiguration);

        long after = System.currentTimeMillis();

        _logger.debug("Fetch page, took: " + (after - before) + " ms");

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

    private boolean allowToCrawlInformation(CrawledPage page, CrawlContext crawlContext) {
        CrawlDecision crawlDecision = crawlDecisionMaker.downloadPageContentDecision(page, crawlContext);

        if (!crawlDecision.isAllow()) {
            _logger.debug("Information on page " + page.getAbsoluteUrl() + " is not crawled, " + crawlDecision.getReason());
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
        List<String> urls = hyperLinkParser.getUrls(page, null);

        List<PageToCrawl> pageToSchedule = new ArrayList<>();
        _logger.debug("Scheduling...");
        if (urls != null) {
            /* Total amount of free memory available to the JVM */
            _logger.debug("Free memory available before schedule (bytes): "
                    + Runtime.getRuntime().freeMemory());

            /* Total memory currently in use by the JVM */
            _logger.debug("Total memory in use by JVM before schedule (bytes): "
                    + Runtime.getRuntime().totalMemory());
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

                    if (allowToCrawlPage(pageToCrawl, crawlContext)) {
                        crawlContext.addQueuedPage(url);
                        pageToSchedule.add(pageToCrawl);
                    }


                } catch (MalformedURLException ex) {
                    java.util.logging.Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            /* Total amount of free memory available to the JVM */
            _logger.debug("Free memory available after schedule (bytes): "
                    + Runtime.getRuntime().freeMemory());

            /* Total memory currently in use by the JVM */
            _logger.debug("Total memory in use after schedule (bytes): "
                    + Runtime.getRuntime().totalMemory());
            scheduler.addPagesToCrawl(pageToSchedule);
            _logger.debug("Schedule completed!");
        }
    }

    private void scrapInformation(CrawledPage page) {
        _logger.debug("Scrapping...");
        scrapper.getInnerText(page);
        _logger.debug("Scrap completed!");
    }

    @Override
    public void run() {
        crawl();
    }
}
