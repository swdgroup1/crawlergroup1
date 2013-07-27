/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 3:08:48 PM Jul 16, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import wcrawler.information.CrawlConfiguration;
/*
 * Commit again 
 */
public class CrawlConfigurationHandler {

    //file path must config in C while system deploy
    private String filePath = "E:\\config.xml";
    // load crawl configuration from file config.xml

    public CrawlConfiguration loadCrawlConfigFromXml() {
        CrawlConfiguration crawlConfiguration = new CrawlConfiguration();
        try {
            File file = new File(filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            // normalize text representation
            document.getDocumentElement().normalize();
            //Get element "config" into NodeList
            NodeList nodeList = document.getElementsByTagName("config");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                //Check if Node is Element node
                String politenessDelay = "";
                String connectionTimeout = "";
                String maxConnectionPerHost = "";
                String maxDownloadPageSize = "";
                String maxConcurrentThread = "";
                String maxTotalConnections ="";
                String politenessPolicyEnable="";
                 //for my sql database
                String driver ="";
                String url ="";
                String username ="";
                String password="";
                String database="";
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    //Set value 
                    politenessDelay = getValue("politenessDelay", element);
                    connectionTimeout = getValue("connectionTimeout", element);
                    maxConnectionPerHost = getValue("maxConnectionPerHost", element);
                    maxDownloadPageSize = getValue("maxDownloadPageSize", element);
                    maxConcurrentThread = getValue("maxConcurrentThread", element);
                    maxTotalConnections = getValue("maxTotalConnections", element);
                    politenessPolicyEnable = getValue("politenessPolicyEnable", element);
                    //For connnect Mysql Database
                    driver = getValue("driver", element);
                    url = getValue("url", element);
                    username= getValue("username", element);
                    password = getValue("password", element);
                    database = getValue("database", element);
                }
                //Set value into  crawlConfiguration
                crawlConfiguration.setPolitenessDelay(Integer.parseInt(politenessDelay));
                crawlConfiguration.setConnectionTimeout(Integer.parseInt(connectionTimeout));
                crawlConfiguration.setMaxConnectionPerHost(Integer.parseInt(maxConnectionPerHost));
                crawlConfiguration.setMaxDownloadPageSize(Integer.parseInt(maxDownloadPageSize));
                crawlConfiguration.setMaxConcurrentThread(Integer.parseInt(maxConcurrentThread));
                crawlConfiguration.setMaxTotalConnections(Integer.parseInt(maxTotalConnections));
                crawlConfiguration.setPolitenessPolicyEnable("true".equals(politenessPolicyEnable.toLowerCase())? true: false);
                //Set value for database
                crawlConfiguration.setDriver(driver);
                crawlConfiguration.setUrl(url);
                crawlConfiguration.setUsername(username);
                crawlConfiguration.setPassword(password);
                crawlConfiguration.setDatabase(database);
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(CrawlConfigurationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return crawlConfiguration;
    }

    private String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    /**
     * update crawl config file
     *
     * @param itemname
     * @param value
     * @return
     */
    public boolean updateCrawlConfigXml(String itemname, int value) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);

            //Get the config element by tag name 
            Node config = document.getElementsByTagName("config").item(0);
            //Loop the config child node
            NodeList list = config.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                //Get the Element and update value
                if (itemname.equals(node.getNodeName())) {
                    node.setTextContent(String.valueOf(value));
                }
            }
            //Write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            //Holder to transformer result
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerConfigurationException ex) {
            Logger.getLogger(CrawlConfigurationHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(CrawlConfigurationHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
