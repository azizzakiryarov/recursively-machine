package se.azza.recursivelymachine.utility;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

public class PrintUtility {

    static Logger logger = Logger.getLogger(String.valueOf(PrintUtility.class));

    public static void print(String msg, Object... args) {
        logger.info(String.format(msg, args));
    }

    public static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }

    public static void loggerInfoThreadAndUrl(Element src){
        logger.info("Thread: " + Thread.currentThread().getName() + " URL: " + src.attr("abs:src"));
    }
}
