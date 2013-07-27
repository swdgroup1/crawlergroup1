/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:32:07 AM Jul 12, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.core;

import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import wcrawler._interface.IPageRequester;
import wcrawler.information.CrawlConfiguration;
import wcrawler.information.CrawledPage;
import wcrawler.information.PageToCrawl;

/**
 *
 * @author TanNhat
 */
public class PageRequester implements IPageRequester {

    private static Logger _logger = Logger.getLogger(PageRequester.class);
    protected long lastFetchTime = 0;
    protected final Object mutex = new Object();

    @Override
    public CrawledPage fetchPage(PageToCrawl page, CrawlConfiguration config) {
        URL url;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        _logger.info("About to fetch page: \"" + page.getAbsoluteUrl() + "\"");

        try {
            long bf = System.currentTimeMillis();
            synchronized (mutex) {
                long delayTime = config.getPolitenessDelay()*1000 - ((new Date()).getTime() - lastFetchTime);
                if (delayTime>0) {
                    Thread.sleep(delayTime);
                }

                long af = System.currentTimeMillis();
                _logger.debug("Delay in " + delayTime + " ms");
            }

            // create the HttpURLConnection
            url = new URL(page.getAbsoluteUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            lastFetchTime = (new Date()).getTime();
            // just want to do an HTTP GET
            connection.setRequestMethod("GET");

            // wait to get response until reach configured timeout 
            connection.setReadTimeout(config.getConnectionTimeout() * 1000);
//            connection.setReadTimeout(10000);
//            connection.connect();


            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                _logger.debug("Get problem with connection: " + connection.getResponseMessage());
                return null;
            }

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            // get raw html from response
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            CrawledPage crawledPage = new CrawledPage();

            crawledPage.setContentType(connection.getContentType());
            crawledPage.setContentEncoding(connection.getContentEncoding());
            crawledPage.setRawContent(stringBuilder.toString());
            crawledPage.setResponseCode(connection.getResponseCode());
            crawledPage.setResponseMessage(connection.getResponseMessage());
            crawledPage.setPageSizeInBytes(stringBuilder.toString().getBytes("UTF-8").length);

            return crawledPage;

        } catch (ProtocolException pe) {
            _logger.debug("Get problem to set GET request method for connection. Exception: " + pe.getMessage(), pe);
        } catch (SocketTimeoutException ste) {
            _logger.debug("Cannot establish connection to \"" + page.getAbsoluteUrl() + "\" , Timeout expires. Exception: " + ste.getMessage(), ste);
        } catch (IOException ioe) {
            _logger.debug("Cannot open connection to \"" + page.getAbsoluteUrl() + "\". Exception: " + ioe.getMessage(), ioe);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(PageRequester.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    // log this exception
                    _logger.debug("Cannot close BufferedReader instance! Exception: " + ioe.getMessage(), ioe);
                }
            }
        }
        return null;
    }
//    public static void main(String[] args) throws MalformedURLException {
//        String url = "http://www.drugs.com";
//        PageToCrawl page = new PageToCrawl();
//        URL _url = new URL(url);
//        page.setAbsoluteUrl(url);
//        page.setIsRoot(true);
//        page.setUrl(_url);
//
//        PageRequester pageRequester = new PageRequester();
//        CrawledPage crawledPage = pageRequester.fetchPage(page, null);
//    }
}
