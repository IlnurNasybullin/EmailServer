package org.example.app.filters;

public interface FilterData {
    String AUTHENTICATION_SERVER = "http://host.docker.internal:9000/";
    String REGISTRATION = "registration";
    String SIGN_IN = "sign";
    String AUTHENTICATION = "authorize";
    String EXIT = "exit";

    String GET_EMAIL = "getEmail";

    String BAD_PAGE_URL = "http://localhost:9001/error/";

    String COOKIE = "Cookie";

    static String getBadPageURL(int status) {
        return BAD_PAGE_URL + status;
    }
}
