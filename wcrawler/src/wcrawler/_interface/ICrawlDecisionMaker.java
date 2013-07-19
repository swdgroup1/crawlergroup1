/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:24:54 AM Jul 12, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler._interface;

import wcrawler.information.CrawlContext;
import wcrawler.information.CrawlDecision;
import wcrawler.information.CrawledPage;
import wcrawler.information.PageToCrawl;

public interface ICrawlDecisionMaker {
    // gets decision to crawl a page

    CrawlDecision crawlPageDecision(PageToCrawl page, CrawlContext context);

    // gets decision to crawl link from a page
    CrawlDecision crawlPageLinksDecision(CrawledPage page, CrawlContext context);

    // gets decision to download content from a page
    CrawlDecision downloadPageContentDecision(CrawledPage page, CrawlContext context);
}
