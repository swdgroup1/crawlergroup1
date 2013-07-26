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
    private ArrayList<String> disallows;
    private ArrayList<String> allows;
    private int crawlDelay;

    public RobotstxtRecord() {
        userAgents = new ArrayList<>();
        disallows = new ArrayList<>();
        allows = new ArrayList<>();
        crawlDelay = -1;
    }

    public ArrayList<String> getUserAgents() {
        return userAgents;
    }

    public void setUserAgents(ArrayList<String> userAgents) {
        this.userAgents = userAgents;
    }

    public ArrayList<String> getDisallows() {
        return disallows;
    }

    public void setDisallows(ArrayList<String> disallows) {
        this.disallows = disallows;
    }

    public ArrayList<String> getAllow() {
        return allows;
    }

    public void setAllow(ArrayList<String> allows) {
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
