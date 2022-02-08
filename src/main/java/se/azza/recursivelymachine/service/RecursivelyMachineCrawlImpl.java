package se.azza.recursivelymachine.service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.apache.log4j.Logger;
import se.azza.recursivelymachine.model.RecursivelyMachine;

import static se.azza.recursivelymachine.utility.PrintUtility.*;

public class RecursivelyMachineCrawlImpl implements RecursivelyMachineCrawl {

    static Logger logger = Logger.getLogger(String.valueOf(RecursivelyMachineCrawlImpl.class));

    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private final Set<String> links = new HashSet<>();
    private final RecursivelyMachine recursivelyMachine = new RecursivelyMachine();
    private final RecursivelyMachineSaverImpl recursivelyMachineSaver = new RecursivelyMachineSaverImpl();
    private final Set<String> pagesVisited = recursivelyMachine.getPagesVisited();
    private final Set<String> pagesToVisit = recursivelyMachine.getPagesToVisit();

    /**
     * Here we are going to get all links from our website
     */
    public void searchValidLinks(String url) throws IOException {
        while (!url.isEmpty() && (!pagesToVisit.contains("https://tretton37.com/") && pagesToVisit.size() != 1)) {
            String currentUrl;
            if (pagesToVisit.isEmpty()) {
                currentUrl = url;
                pagesVisited.add(url);
            } else {
                currentUrl = nextUrl(url);
            }
            logger.info("Thread: " + Thread.currentThread().getName() + " URL: " + currentUrl);
            if (Jsoup.isValid(currentUrl, Safelist.basicWithImages())) {
                crawling(currentUrl);
            }
            pagesToVisit.addAll(getLinks());
        }
        logger.info("***Done** Visited " + getLinks().size() + " web page(s)");
    }

    /**
     * Returns the next URL to visit (in the order that they were found). We also do a check to make
     * sure this method doesn't return a URL that has already been visited.
     */

    private String nextUrl(String url) {
        if(pagesToVisit.contains(url)) {
            pagesToVisit.remove(url);
            pagesVisited.add(url);
        }
        return url;
    }

    public void crawling(String url) throws IOException {
        if (Jsoup.isValid(url, Safelist.basicWithImages())) {
            if (links.contains(url)) {
                return;
            }
            print("Fetching %s...", url);
            links.add(url);
            Document doc;
            try {
                doc = Jsoup.connect(url).userAgent(USER_AGENT).get();
            } catch (UnsupportedMimeTypeException e) {
                logger.error("Unsupported Mime type. Aborting crawling for URL: " + url);
                return;
            } catch (MalformedURLException e) {
                logger.error("Unsupported protocol for URL: " + url);
                return;
            } catch (HttpStatusException e) {
                logger.error("Error (status=" + e.getStatusCode() + ") fetching URL: " + url);
                return;
            } catch (IOException e) {
                logger.error("Timeout fetching URL: " + url);
                return;
            }

            Elements media = doc.select("[src]");
            Elements imports = doc.select("link[href]");
            Elements docs = doc.select("a[href]");

            getAndDownloadMedia(media);
            getAndDownloadImports(imports);
            getAndDownloadDocs(docs, url);
        }
    }

    private void getAndDownloadMedia(Elements media){
        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img")) {
                loggerInfoThreadAndUrl(src);
                recursivelyMachineSaver.downloadResource(src.attr("abs:src"));
            } else {
                loggerInfoThreadAndUrl(src);
                recursivelyMachineSaver.downloadResource(src.attr("abs:src"));
            }
        }
    }


    private void getAndDownloadImports(Elements imports) {
        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            loggerInfoThreadAndUrl(link);
            recursivelyMachineSaver.downloadResource(link.attr("abs:src"));
        }
    }

    private void getAndDownloadDocs(Elements docs, String url) throws IOException {
        print("\nLinks: (%d)", docs.size());
        for (Element doc : docs) {
            loggerInfoThreadAndUrl(doc);
            recursivelyMachineSaver.downloadHtml(doc.attr("abs:src"));
            String href = doc.attr("abs:href");
            URL hrefURL = null;
            try {
                hrefURL = new URL(href);
            } catch (MalformedURLException e) {
                logger.error(e.getMessage() + e);
            }
            if (hrefURL != null && hrefURL.getHost().equals(new URL(url).getHost())) {
                crawling(href);
            }
        }
    }

    public Set<String> getLinks() {
        return this.links;
    }
}