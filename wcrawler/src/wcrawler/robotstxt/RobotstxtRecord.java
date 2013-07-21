/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wcrawler.robotstxt;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author Hai
 */
public class RobotstxtRecord {
    private ArrayList<String> userAgents;
    private ConcurrentSkipListSet<String> disallows;
    private ConcurrentSkipListSet<String> allows;
    private int crawlDelay;

    public RobotstxtRecord() {
        userAgents = new ArrayList<>();
        disallows = new ConcurrentSkipListSet<>();
        allows = new ConcurrentSkipListSet<>();
        crawlDelay = -1;
    }

    public ArrayList<String> getUserAgents() {
        return userAgents;
    }

    public void setUserAgents(ArrayList<String> userAgents) {
        this.userAgents = userAgents;
    }

    public ConcurrentSkipListSet<String> getDisallows() {
        return disallows;
    }

    public void setDisallows(ConcurrentSkipListSet<String> disallows) {
        this.disallows = disallows;
    }

    public ConcurrentSkipListSet<String> getAllow() {
        return allows;
    }

    public void setAllow(ConcurrentSkipListSet<String> allows) {
        this.allows = allows;
    }

    public int getCrawlDelay() {
        return crawlDelay;
    }

    public void setCrawlDelay(int crawlDelay) {
        this.crawlDelay = crawlDelay;
    }
    
    public void addDisallow(String disallow){
        disallows.add(disallow);
    }
    
    public void addAllow(String allow){
        allows.add(allow);
    }
    
    public void addUserAgent(String userAgent){
        userAgents.add(userAgent);
    }
}
