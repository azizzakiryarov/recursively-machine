package se.azza.recursivelymachine.utility;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtility {

    /**
     * Checks the syntax of the given URL.
     *
     * @param url The URL.
     * @return true, if valid.
     */
    public static boolean isUriAbsolute(String url) {
        try {
            URI uri = new URI(url);
            return uri.isAbsolute();
        } catch (URISyntaxException e) {
            return false;
        }
    }
}