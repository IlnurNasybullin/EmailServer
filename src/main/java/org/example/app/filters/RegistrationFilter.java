package org.example.app.filters;

import org.example.app.loggers.MyFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static org.example.app.filters.FilterData.*;

@WebFilter("/email/registration")
public class RegistrationFilter extends AuthorizationFilter {

    public final static Logger logger = Logger.getLogger(RegistrationFilter.class.getName());
    private static final String INCORRECT_REGISTRATION_URL = "http://localhost:9001/email/registration_form?isCorrect=false";

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new MyFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("User send response to register him/her");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpURLConnection connection = getConnection();
        logger.info("Connection is set with authenticate server");

        try {
            sendData(connection, request);
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
            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Expires", "0");
            logger.info("Cookie is set. Response is send to controller");
            filterChain.doFilter(request, response);
        } else {
            logger.log(Level.WARNING, "User data is not unique! Response send redirect to ", INCORRECT_REGISTRATION_URL);
            response.sendRedirect(INCORRECT_REGISTRATION_URL);
        }
    }

    private int getCorrectStatus() {
        return SC_CREATED;
    }

    private void sendData(HttpURLConnection connection, HttpServletRequest request) throws IOException {
        logger.log(Level.INFO, "Data are sending with format", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        connection.setDoOutput(true);
        connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        byte[] bytes = request.getInputStream().readAllBytes();

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        connection.connect();
        logger.info("Data are send to authentication server");
    }

    private HttpURLConnection getConnection() throws IOException {
        String urlString = AUTHENTICATION_SERVER + REGISTRATION;
        logger.log(Level.INFO, "Connection is setting with authenticate server: ", urlString);
        URL url = new URL(urlString);
        return (HttpURLConnection) url.openConnection();
    }
}
