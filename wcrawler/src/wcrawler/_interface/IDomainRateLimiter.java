/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 12:11:46 AM Jul 18, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler._interface;

import java.net.URL;

public interface IDomainRateLimiter {

    // applies rate limter to respect politeness policy
    void rateLimit(URL url);

    // add a domain and apply "rate limit" based on crawl delay (default or from robots.txt
    void addDomain(URL url, long minCrawlDelayInMiliSecs);
}
