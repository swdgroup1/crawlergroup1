/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 3:20:01 PM  Jul 16, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler.robotstxt;

// information from robots.txt file

import java.util.ArrayList;

public class Robotstxt {
    //properties are not defined yet 
    ArrayList<RobotstxtRecord> records;

    public Robotstxt() {
        records = new ArrayList<>();
    }

    public ArrayList<RobotstxtRecord> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<RobotstxtRecord> records) {
        this.records = records;
    }
    
    public void addRecord(RobotstxtRecord record){
        records.add(record);
    }
    
    public RobotstxtRecord getRecord(String userAgent){
        RobotstxtRecord other = null;
        
        for(RobotstxtRecord record:records){
            if(record.getUserAgents().contains(userAgent)){//match specific user agent
                return record;
            }
            if(record.getUserAgents().contains("/")){//general user agents
                other = record;
            }
        }
        return other;
    }
}