package org.example.app.filters;

import javax.servlet.http.HttpServletResponse;
import java.util.EnumSet;
import java.util.Set;

public interface FilterData {
    String AUTHENTICATION_SERVER = "http://localhost:9000/";
    String REGISTRATION = "registration";
    String SIGN_IN = "sign";
    String AUTHENTICATION = "authentication";
    String EXIT = "exit";

    Set<Integer> badStatuses = Set.of(HttpServletResponse.SC_BAD_REQUEST, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}
