package se.azza.recursivelymachine.service;

import java.io.IOException;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.log4j.Logger;

import static org.jsoup.Jsoup.connect;

public class RecursivelyMachineService {

    static Logger logger = Logger.getLogger(String.valueOf(RecursivelyMachineService.class));

    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    //private final List<String> links = new LinkedList<>();
    private final Set<String> links = new HashSet<>();
    Connection connection;

    public void getPageLinks(String url) {
        if (!links.contains(url)) {
            try {
                connection = connect(url).userAgent(USER_AGENT);
                links.add(url);
                logger.info("***Visiting*** Received web page at " + url);

                Document document = connection.get();
                Elements linksOnPage = document.select("a[href]");
                    if (connection.response().statusCode() == 200 && Objects.requireNonNull(connection.response().contentType()).contains("text/html")){
                        for (Element page : linksOnPage) {
                            logger.info("Found (" + page.baseUri() + ") link");
                            getPageLinks(page.attr("abs:href"));
                        }
                    } else {
                        logger.info("*** Didn't received*** web page at " + url + " reason: " + connection.response().statusCode());
                    }
            } catch (IOException e) {
                logger.error("For '" + url + "': " + e.getMessage());
            }
        }
    }

    public Set<String> getLinks() {
        return this.links;
    }
}