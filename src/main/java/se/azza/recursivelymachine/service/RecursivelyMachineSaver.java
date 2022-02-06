package se.azza.recursivelymachine.service;

import java.io.IOException;

public interface RecursivelyMachineSaver {

    void downloadResource (String url);

    void downloadHtml(String webpage) throws IOException;

    void createDir(String url);

    String getOnlyUrlText(String urlText);

}
