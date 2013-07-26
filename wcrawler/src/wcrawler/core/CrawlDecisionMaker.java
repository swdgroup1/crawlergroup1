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

    private ConcurrentSkipListSet<String> robotstxtAllow;
    private ConcurrentSkipListSet<String> robotstxtDisallow;
    private ConcurrentSkipListSet<String> containLinkPattern;
    private ConcurrentSkipListSet<String> containInformationPattern;
    private ConcurrentSkipListSet<String> filterAllow;
    private ConcurrentSkipListSet<String> filterDisallow;

    public CrawlDecisionMaker(ConcurrentSkipListSet<String> robotstxtAllow, ConcurrentSkipListSet<String> robotstxtDisallow, ConcurrentSkipListSet<String> containLinkPattern, ConcurrentSkipListSet<String> containInformationPattern, ConcurrentSkipListSet<String> filterAllow, ConcurrentSkipListSet<String> filterDisallow) {
        this.robotstxtAllow = robotstxtAllow != null ? robotstxtAllow : new ConcurrentSkipListSet<String>();
        this.robotstxtDisallow = robotstxtDisallow != null ? robotstxtDisallow : new ConcurrentSkipListSet<String>();
        this.containLinkPattern = containLinkPattern != null ? containLinkPattern : new ConcurrentSkipListSet<String>();
        this.containInformationPattern = containInformationPattern != null ? containInformationPattern : new ConcurrentSkipListSet<String>();
        this.filterAllow = filterAllow != null ? filterAllow : new ConcurrentSkipListSet<String>();
        this.filterDisallow = filterDisallow != null ? filterDisallow : new ConcurrentSkipListSet<String>();
    }

    //check whether an url is in a list
    private boolean isMatchedPattern(String absoluteUrl, ConcurrentSkipListSet<String> patternList) {
        if (patternList.size() < 0) {
            return false;
        }

        for (String pattern : patternList) {
            if (absoluteUrl.startsWith(pattern)) {
                return true;
            }
        }

        return false;
    }

    //check whether an url is allowed according to a set of allow and disallow rules
    private boolean isAllowed(String absoluteUrl, ConcurrentSkipListSet<String> allows, ConcurrentSkipListSet<String> disallows) {
        //find the longest matched allow rule
        String longestAllow = "";
        for (String allow : allows) {
            if (absoluteUrl.startsWith(allow)
                    && allow.length() > longestAllow.length()) {
                longestAllow = allow;
            }
        }

        //find the longest matched disallow rule
        String longestDisallow = "";
        for (String disallow : disallows) {
            if (absoluteUrl.startsWith(disallow)
                    && disallow.length() > longestDisallow.length()) {
                longestDisallow = disallow;
            }
        }

        //the longest matched rule will be the choosen one
        return longestAllow.length() >= longestDisallow.length();
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

        /**
         * An url is downloaded if only it haven't download before; satisfy
         * allow/disallow rules provided in robots.txt and by user; contain urls
         * which may need to be download or information to scrap
         */
        if (isMatchedPattern(absoluteUrl, queuedPages)) { //Queued before
            decision.setReason("All ready queued");
        } else if (!isAllowed(absoluteUrl, robotstxtAllow, robotstxtDisallow)) { //Not permited by robots.txt
            decision.setReason("Disallow by robots.txt");
        } else if (!isAllowed(absoluteUrl, filterAllow, filterDisallow)) { //Not permited by filter pattern
            decision.setReason("Disallow by users' filter pattern");
        } else if (!isMatchedPattern(absoluteUrl, containLinkPattern)
                && !isMatchedPattern(absoluteUrl, containInformationPattern)) { //Not contains urls nor information
            decision.setReason("Contain no useful information");
        } else {
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

        decision.setAllow(true);
        return decision;
    }

    @Override
    //check whether an url is contain useful information to scrap
    public CrawlDecision downloadPageContentDecision(CrawledPage page, CrawlContext context) {
        String absoluteUrl = page.getAbsoluteUrl();

        CrawlDecision decision = new CrawlDecision();
        decision.setAllow(isMatchedPattern(absoluteUrl, containInformationPattern));

        decision.setAllow(true);
        return decision;
    }
}
