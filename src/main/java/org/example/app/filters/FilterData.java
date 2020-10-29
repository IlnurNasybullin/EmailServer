package org.example.app.filters;

import javax.servlet.http.HttpServletResponse;
import java.util.EnumSet;
import java.util.Set;

public interface FilterData {
    String AUTHENTICATION_SERVER = "http://localhost:9000/";
    String REGISTRATION = "registration";
    String SIGN_IN = "sign";
    String AUTHENTICATION = "authorize";
    String EXIT = "exit";

    String BAD_PAGE_URL = "http://localhost:9001/error/";

    String COOKIE = "Cookie";

    static String getBadPageURL(int status) {
        return BAD_PAGE_URL + status;
    }
}
