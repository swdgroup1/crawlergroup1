/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:31:38 AM Jul 12, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.core;

import java.util.concurrent.ConcurrentSkipListSet;
import wcrawler._interface.ICrawlDecisionMaker;
import wcrawler.information.CrawlContext;
import wcrawler.information.CrawlDecision;
import wcrawler.information.CrawledPage;
import wcrawler.information.PageToCrawl;

public class CrawlDecisionMaker implements ICrawlDecisionMaker {

    private ConcurrentSkipListSet<String> spiderTrap;
    private ConcurrentSkipListSet<String> containLinkPattern;
    private ConcurrentSkipListSet<String> containInformationPattern;
    private ConcurrentSkipListSet<String> filterPattern;

    //check whether an url is in a list
    private boolean isMatchedPattern(String absoluteUrl, ConcurrentSkipListSet<String> patternList) {
        for(String pattern:patternList){
            if(absoluteUrl.startsWith(pattern)){
                return true;
            }
        }

        return false;
    }

    @Override
    //check whether an url is needed to be downloaded
    public CrawlDecision crawlPageDecision(PageToCrawl page, CrawlContext context) {
        String absoluteUrl = page.getAbsoluteUrl();

        //get all queued pages at current time
        ConcurrentSkipListSet<String> queuedPages = context.getQueuedPages();

        CrawlDecision decision = new CrawlDecision();
        
        //default decision is NOT
        decision.setAllow(false);

        if (queuedPages.contains(absoluteUrl)) {
            decision.setReason("All ready queued");
        } else if (spiderTrap.contains(absoluteUrl)) {
            decision.setReason("Spider Trap");
        } else if (!isMatchedPattern(absoluteUrl, filterPattern)) {
            decision.setReason("Not match pattern");
        } else {//pass all test
            decision.setAllow(true);
        }

        return decision;
    }

    @Override
    //check whether an url is contain useful hyperlinks
    public CrawlDecision crawlPageLinksDecision(CrawledPage page, CrawlContext context) {
        String absoluteUrl = page.getAbsoluteUrl();

        CrawlDecision decision = new CrawlDecision();
        decision.setAllow(isMatchedPattern(absoluteUrl, containLinkPattern));

        return decision;
    }

    @Override
    //check whether an url is contain useful information to scrap
    public CrawlDecision downloadPageContentDecision(CrawledPage page, CrawlContext context) {
        String absoluteUrl = page.getAbsoluteUrl();

        CrawlDecision decision = new CrawlDecision();
        decision.setAllow(isMatchedPattern(absoluteUrl, containInformationPattern));

        return decision;
    }
}
