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


public class RecursivelyMachineCrawlImpl implements RecursivelyMachineCrawl {

    static Logger logger = Logger.getLogger(String.valueOf(RecursivelyMachineCrawlImpl.class));

    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private final Set<String> links = new HashSet<>();
    private final Set<String> pagesVisited = new HashSet<>();
    private final Set<String> pagesToVisit = new HashSet<>();
    RecursivelyMachineSaverImpl recursivelyMachineSaver = new RecursivelyMachineSaverImpl();

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
        logger.info("***Done** Visited " + pagesVisited.size() + " web page(s)");
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

            print("\nMedia: (%d)", media.size());
            for (Element src : media) {
                if (src.tagName().equals("img")) {
                    print(" * %s: <%s> %sx%s (%s)",
                            src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                            trim(src.attr("alt"), 20));
                    logger.info(src.attr("abs:src"));
                    recursivelyMachineSaver.downloadResource(src.attr("abs:src"));
                } else {
                    print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
                    logger.info(src.attr("abs:src"));
                    recursivelyMachineSaver.downloadResource(src.attr("abs:src"));
                }
            }

            print("\nImports: (%d)", imports.size());
            for (Element link : imports) {
                print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"), link.attr("rel"));
                logger.info(link.attr("abs:src"));
                recursivelyMachineSaver.downloadResource(link.attr("abs:src"));
            }

            print("\nLinks: (%d)", docs.size());
            for (Element link : docs) {
                print(" * a: <%s> (%s)", link.attr("abs:href"), trim(link.text(), 35));
                recursivelyMachineSaver.downloadHtml(link.attr("abs:src"));
                String href = link.attr("abs:href");
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
    }

    private static void print(String msg, Object... args) {
        logger.info(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }

    public Set<String> getLinks() {
        return this.links;
    }
}