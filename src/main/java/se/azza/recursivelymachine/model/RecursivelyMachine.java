package se.azza.recursivelymachine.model;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import se.azza.recursivelymachine.service.RecursivelyMachineService;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RecursivelyMachine {

    static Logger logger = Logger.getLogger(String.valueOf(RecursivelyMachine.class));

    private final Set<String> pagesVisited = new HashSet<>();
    private final List<String> pagesToVisit = new LinkedList<>();

    /**
     * Here we are going to get all links
     */
    public void searchAllLinks(String url) {
        while (!url.isEmpty()) {
            String currentUrl;
            RecursivelyMachineService recursivelyMachineService = new RecursivelyMachineService();
            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesVisited.add(url);
            } else {
                currentUrl = this.nextUrl();
                logger.info("Thread: " + Thread.currentThread().getName() + " URL: " + currentUrl);
            }
            if (Jsoup.isValid(currentUrl, Safelist.basicWithImages())) {
                recursivelyMachineService.getPageLinks(currentUrl);
            }
            this.pagesToVisit.addAll(recursivelyMachineService.getLinks());
        }
        logger.info("***Done** Visited " + this.pagesVisited.size() + " web page(s)");
    }

    /**
     * Returns the next URL to visit (in the order that they were found). We also do a check to make
     * sure this method doesn't return a URL that has already been visited.
     */
    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while (this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }
}
