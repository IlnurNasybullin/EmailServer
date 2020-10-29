package org.example.app.filters;

import org.example.app.loggers.MyFormatter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.example.app.filters.FilterData.getBadPageURL;

@WebFilter(value = "/email/sign")
public class SignInFilter extends AuthorizationFilter {

    public final static Logger logger = Logger.getLogger(SignInFilter.class.getName());
    public static final String INCORRECT_SIGN_IN_URL = "http://localhost:9001/email?isCorrect=false";

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new MyFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    private int getCorrectStatus() {
        return SC_OK;
    }

    private void sendData(HttpURLConnection connection) throws IOException {
        connection.connect();
    }

    private HttpURLConnection getConnection(HttpServletRequest request) throws IOException {
        String urlString = FilterData.AUTHENTICATION_SERVER + FilterData.SIGN_IN + "?" + request.getQueryString();
        logger.log(Level.INFO, "Connection is setting wit authenticate server: ", urlString);
        URL url = new URL(urlString);
        return (HttpURLConnection) url.openConnection();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("User send request for signing in system.");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpURLConnection connection = getConnection(request);
        logger.info("Connection is set with authenticate server");

        try {
            sendData(connection);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception!", e);
            String badPageURL = getBadPageURL(500);

            logger.log(Level.SEVERE, "Redirect url to ", badPageURL);
            response.sendRedirect(badPageURL);
            return;
        }

        int code = connection.getResponseCode();
        logger.log(Level.INFO, "Response code from authentication server" ,code);

        if (code == getCorrectStatus()) {
            setCookie(connection, response);
            logger.info("Cookie is set. Response is send to controller");
            filterChain.doFilter(request, response);
        } else {
            logger.log(Level.WARNING, "User data incorrect! Response send redirect to ", INCORRECT_SIGN_IN_URL);
            response.sendRedirect(INCORRECT_SIGN_IN_URL);
        }
    }
}
