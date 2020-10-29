package org.example.app;

import org.example.app.loggers.MyFormatter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    private static final Logger logger = Logger.getLogger(Test.class.getName());

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new MyFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    public static void main(String[] args) throws IOException {
        logger.info("sur");

        URL url = new URL("http://localhost:9001/email/something?x=x");
        String path = url.getPath();

        Pattern pattern = Pattern.compile("/email/(.+)");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
        } else {
            System.out.println("error");
        }
    }
}
