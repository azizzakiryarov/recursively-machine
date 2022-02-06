package se.azza.recursivelymachine.service;

import java.io.IOException;

public interface RecursivelyMachineCrawl {

    void searchValidLinks(String url) throws IOException;

    void crawling(String url) throws IOException;

}
