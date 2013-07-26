/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:31:04 AM Jul 12, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.information;

import java.util.ArrayList;

public class CrawlConfiguration {
    //List of seeds url

    private ArrayList<String> seedList;
    // Crawl delay, crawler has to respect sites rule
    private int politenessDelay;
    // how long will connection timeout in case it cannot make connection
    private int connectionTimeout;
    // max connection to a host
    private int maxConnectionPerHost;
    // mas total connection
    private int maxTotalConnections;
    // the maximum size of a page
    private int maxDownloadPageSize;
    // the maximum concurrent thread of running crawler
    private int maxConcurrentThread;
    private boolean politenessPolicyEnable;

    public ArrayList<String> getSeedList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("www.drugs.com");
        list.add("en.wikipedia.com");
        return list;
        //return seedList;
    }

    public void setSeedList(ArrayList<String> seedList) {
        this.seedList = seedList;
    }

    public boolean isPolitenessPolicyEnable() {
        return politenessPolicyEnable;
    }

    public void setPolitenessPolicyEnable(boolean politenessPolicyEnable) {
        this.politenessPolicyEnable = politenessPolicyEnable;
    }

    public int getPolitenessDelay() {
        return politenessDelay;
    }

    public void setPolitenessDelay(int politenessDelay) {
        this.politenessDelay = politenessDelay;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getMaxConnectionPerHost() {
        return maxConnectionPerHost;
    }

    public void setMaxConnectionPerHost(int maxConnectionPerHost) {
        this.maxConnectionPerHost = maxConnectionPerHost;
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public int getMaxDownloadPageSize() {
        return maxDownloadPageSize;
    }

    public void setMaxDownloadPageSize(int maxDownloadPageSize) {
        this.maxDownloadPageSize = maxDownloadPageSize;
    }

    public int getMaxConcurrentThread() {
        return maxConcurrentThread;
    }

    public void setMaxConcurrentThread(int maxConcurrentThread) {
        this.maxConcurrentThread = maxConcurrentThread;
    }
}
