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

import java.util.Iterator;
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

    private boolean isMatchedPattern(String absoluteUrl, ConcurrentSkipListSet<String> patternList) {
        //
        for(String s:patternList){
            
        }
        
        Iterator<String> it = patternList.iterator();
        String pattern;

        while (it.hasNext()) {
            pattern = it.next();
            if (absoluteUrl.startsWith(pattern)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public CrawlDecision crawlPageDecision(PageToCrawl page, CrawlContext context) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String absoluteUrl = page.getAbsoluteUrl();

        ConcurrentSkipListSet<String> queuedPages = context.getQueuedPages();

        CrawlDecision decision = new CrawlDecision();
        decision.setAllow(false);

        if (queuedPages.contains(absoluteUrl)) {
            decision.setReason("All ready queued");
        } else if (spiderTrap.contains(absoluteUrl)) {
            decision.setReason("Spider Trap");
        } else if (!isMatchedPattern(absoluteUrl, filterPattern)) {
            decision.setReason("Not match pattern");
        } else {
            decision.setAllow(true);
        }

        return decision;
    }

    @Override
    public CrawlDecision crawlPageLinksDecision(CrawledPage page, CrawlContext context) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String absoluteUrl = page.getAbsoluteUrl();

        CrawlDecision decision = new CrawlDecision();
        decision.setAllow(isMatchedPattern(absoluteUrl, containLinkPattern));

        return decision;
    }

    @Override
    public CrawlDecision downloadPageContentDecision(CrawledPage page, CrawlContext context) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String absoluteUrl = page.getAbsoluteUrl();

        CrawlDecision decision = new CrawlDecision();
        decision.setAllow(isMatchedPattern(absoluteUrl, containInformationPattern));

        return decision;
    }
}
