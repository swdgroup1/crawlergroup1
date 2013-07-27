
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 3:18:27 PM  Jul 16, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler.robotstxt;

// Read data from file robots.txt of website

import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class RobotstxtLoader {
    private StringTokenizer tokenizer;
    
    //take a non-comment line from robots.txt
    private String nextLine(){
        String line;
        while(tokenizer.hasMoreTokens()){
            line = tokenizer.nextToken();//get one line
            
            //remove comment
            int commentPos = line.indexOf("#");
            if(commentPos!=-1){
                line = line.substring(0,commentPos);
            }
            line = line.trim();
            if(line.length()==0){//emtpy line or comment only
                continue;
            }
            return line;
        }
        return null;
    }
    public Robotstxt loadRobotstxt(String content){
        
        Robotstxt robotstxt = new Robotstxt();
        
        //Document doc = Jsoup.parse(content);
        
//        Elements contentElement = doc.select("div.contentBox");
//        Elements titleElement = doc.select("div.titleAudio clearAfter > h1");
//               
//        String drugsInfo = contentElement.text();
//        
//        String title = titleElement.text();
        //content = doc.text();                
        
        //parse the file line by line
        tokenizer = new StringTokenizer(content, "\n");
        
        String line = nextLine();
        //parse the file record by record
        while(line!=null){
            //ignore all un-supported directives before a new record
            while(line != null && !line.startsWith("User-agent:")){
                line = nextLine();
            }
            
            //start new record
            RobotstxtRecord record = new RobotstxtRecord();
            System.out.println(line);
            //get all user agents, they are always on top of a record
            while(line!=null && line.startsWith("User-agent:")){
                record.addUserAgent(line.substring(11).trim());
                line = nextLine();
            }
            
            //get all another directives
            while(line!=null&&!line.startsWith("User-agent:")){
                if(line.startsWith("Disallow:")){
                    record.addDisallow(line.substring(9).trim());
                }
                if(line.startsWith("Allow:")){
                    record.addAllow(line.substring(6).trim());
                }
                if(line.startsWith("Crawl-delay:")){
                    record.setCrawlDelay(Integer.parseInt(line.substring(12)));
                }
                line = nextLine();
            }
            
            //add record to Robotstxt
            robotstxt.addRecord(record);
        }
        
        return robotstxt;
    }
}
