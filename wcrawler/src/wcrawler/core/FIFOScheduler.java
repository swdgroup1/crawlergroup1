/**
 * Description: This is assignment project of Software Architecture and Design
 * Course The purpose of this project is to build a web crawler to get
 * information from some specific sites. In this case, crawler will get
 * information about drug from www.drugs.com
 */
/**
 * Created at: 10:32:20 AM Jul 12, 2013
 *
 * @author TanNhat Project: wcrawler
 */
package wcrawler.core;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;
import wcrawler._interface.IScheduler;
import static wcrawler.core.FIFOScheduler._logger;
import wcrawler.information.PageToCrawl;

public class FIFOScheduler implements IScheduler {

    static Logger _logger = Logger.getLogger(FIFOScheduler.class);
    private ConcurrentLinkedQueue<PageToCrawl> _pageToCrawl;

    public FIFOScheduler() {
        this._pageToCrawl = new ConcurrentLinkedQueue<>();
    }

    /**
     * Get next page to crawl from queue
     *
     * @return : PageToCrawl
     *
     */
    @Override
    public PageToCrawl getNextPageToCrawl() {

        PageToCrawl pageToCrawl = null;
        if (_pageToCrawl.size() > 0) {
            pageToCrawl = _pageToCrawl.poll();
        }

        return pageToCrawl;
    }

    /**
     * Add a list of pages to concurrent queue
     *
     * @params: List<PageToCrawl> pages
     *
     */
    @Override
    public void addPagesToCrawl(List<PageToCrawl> pages) {
        if (pages == null) {
            throw new NullPointerException("pages");
        }

        if (pages.isEmpty()) {
            _logger.debug("This list of pages is empty");
            return;
        }
       
        for (PageToCrawl p : pages) {
            addPage(p);
        }
        _logger.debug("Queue size: "+_pageToCrawl.size());
    }

    /**
     * Add a page to queue
     *
     * @param: page page to crawl to add to queue
     */
    @Override
    public void addPage(PageToCrawl page) {
        if (page == null) {
            throw new NullPointerException("page");
        }

        _pageToCrawl.add(page);
    }

    /**
     * Clear the queue, create a new queue
     */
    @Override
    public void clear() {
        _pageToCrawl = new ConcurrentLinkedQueue<PageToCrawl>();
    }

    /**
     * Get number of page to crawl in queue
     *
     * @return: count
     */
    @Override
    public long getNumberOfPageToCrawl() {
        int count;
        count = _pageToCrawl.size();

        return count;
    }
}
