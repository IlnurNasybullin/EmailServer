package org.example.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) throws IOException {
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
