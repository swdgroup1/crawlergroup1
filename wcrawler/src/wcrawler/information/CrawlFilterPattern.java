
/**
 * Description: 
 * This is assignment project of Software Architecture and Design Course
 * The purpose of this project is to build a web crawler to get information from
 * some specific sites. In this case, crawler will get information about drug 
 * from www.drugs.com
*/

/**
 * Created at: 4:40:48 PM  Jul 16, 2013
 * @author TanNhat
 * Project: wcrawler
 */
package wcrawler.information;


import java.util.concurrent.ConcurrentSkipListSet;


public class CrawlFilterPattern {
        ConcurrentSkipListSet<String> allows;
        ConcurrentSkipListSet<String> disallows;

    public CrawlFilterPattern() {
        allows = new ConcurrentSkipListSet<>();
        disallows = new ConcurrentSkipListSet<>();
    }

    public ConcurrentSkipListSet<String> getAllows() {
        return allows;
    }

    public void setAllows(ConcurrentSkipListSet<String> allows) {
        this.allows = allows;
    }

    public ConcurrentSkipListSet<String> getDisallows() {
        return disallows;
    }

    public void setDisallows(ConcurrentSkipListSet<String> disallows) {
        this.disallows = disallows;
    }
    
    public void addAllows(String allow){
        allows.add(allow);
    }
    
    public void addDisallows(String allow){
        disallows.add(allow);
    }
}
