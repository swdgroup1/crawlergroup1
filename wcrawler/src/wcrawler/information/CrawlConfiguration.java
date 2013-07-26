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
    private String driver;
    //URL path 
    private String url;
    //username login mysql
    private String username;
    //contain link pattern
    private ArrayList<String> containLinkPattern;
    //contai information pattern
    private ArrayList<String> containInformationPattern;

    public ArrayList<String> getContainLinkPattern() {
        return containLinkPattern;
    }

    public void setContainLinkPattern(ArrayList<String> containLinkPattern) {
        this.containLinkPattern = containLinkPattern;
    }

    public ArrayList<String> getContainInformationPattern() {
        return containInformationPattern;
    }

    public void setContainInformationPattern(ArrayList<String> containInformationPattern) {
        this.containInformationPattern = containInformationPattern;
    }  
        
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
    //password login mysql
    private String password;
    //name database 
    private String database;
    public ArrayList<String> getSeedList() {
        if(seedList!=null){
            return seedList;
        }
        
        ArrayList<String> list = new ArrayList<>();
        list.add("http://www.drugs.com");
        list.add("en.wikipedia.com");
        return list;
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
