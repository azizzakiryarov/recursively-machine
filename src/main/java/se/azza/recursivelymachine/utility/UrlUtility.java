package se.azza.recursivelymachine.utility;

public class UrlUtility {

    public static String getOnlyUrlText(String urlText) {
        if (null != urlText && urlText.length() > 0) {
            int endIndex = urlText.lastIndexOf("/");
            if (endIndex != -1) {
                return urlText.substring(0, endIndex);
            }
        }
        return urlText;
    }
}