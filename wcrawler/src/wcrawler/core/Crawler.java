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
package wcrawler.core;

import org.apache.log4j.Logger;
import wcrawler._interface.ICrawlDecisionMaker;
import wcrawler._interface.IHyperLinkParser;
import wcrawler._interface.IPageRequester;
import wcrawler._interface.IScheduler;
import wcrawler.information.CrawlConfiguration;

public class Crawler {

    private IPageRequester pageRequester;
    private IScheduler scheduler;
    private IHyperLinkParser hyperLinkParser;
    private ICrawlDecisionMaker crawlDecisionMaker;
    private CrawlConfiguration crawlConfiguration;
        
    public Crawler() {
    }

    public Crawler(IPageRequester pageRequester, IScheduler scheduler, IHyperLinkParser hyperLinkParser, ICrawlDecisionMaker crawlDecisionMaker, CrawlConfiguration crawlConfiguration) {
        this.pageRequester = pageRequester;
        this.scheduler = scheduler;
        this.hyperLinkParser = hyperLinkParser;
        this.crawlDecisionMaker = crawlDecisionMaker;
        this.crawlConfiguration = crawlConfiguration;
    }
}
