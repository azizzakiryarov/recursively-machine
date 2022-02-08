package se.azza.recursivelymachine.service;

import org.apache.log4j.Logger;
import se.azza.recursivelymachine.utility.UriUtility;
import se.azza.recursivelymachine.utility.UrlUtility;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RecursivelyMachineSaverImpl implements RecursivelyMachineSaver {

    static Logger logger = Logger.getLogger(String.valueOf(RecursivelyMachineSaverImpl.class));

    @Override
    public void downloadResource(String url) {
        createDir(url);
        if (UriUtility.isUriAbsolute(url)) {
            try (InputStream in = URI.create(url).toURL().openStream()) {
                if (Files.notExists(Path.of(url))) {
                    logger.info("Trying to download: " + url);
                    Files.copy(in, Paths.get(url).toAbsolutePath());
                    logger.info("Successfully downloaded: " + url);
                }
            } catch (IOException e) {
                logger.error(e.getMessage() + e);
            }
        }
    }

    @Override
    public void downloadHtml(String webpage) {
        createDir(webpage);
        if (Files.notExists(Path.of(webpage))) {
            try {
                logger.info("Trying to download: " + webpage);
                URL url = new URL(webpage);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(webpage));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                }
                reader.close();
                writer.close();
                logger.info("Successfully Downloaded: " + webpage);
            } catch (MalformedURLException mue) {
                logger.error("Malformed URL Exception raised" + mue);
            } catch (IOException ie) {
                logger.error("IOException raised" + ie);
            }
        }
    }

    @Override
    public void createDir(String dir) {
        String projectDir = System.getProperty("user.dir");
        String rootDir = projectDir + "/https://tretton37.com/";

        if (Files.notExists(Path.of(rootDir))) {
            try {
                logger.info("Trying to create directory: " + rootDir);
                Files.createDirectories(Path.of(rootDir));
                logger.info("Directory is created directory: " + rootDir);
            } catch (IOException e) {
                logger.error(e.getMessage() + e);
            }
        } else {
            try {
                logger.info("Trying to create directory: " + dir);
                Files.createDirectories(Path.of(UrlUtility.getOnlyUrlText(dir)));
                logger.info("Directory is created directory: " + dir);
            } catch (IOException e) {
                logger.error(e.getMessage() + e);
            }
        }
    }

}