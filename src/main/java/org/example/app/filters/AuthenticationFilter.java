package org.example.app.filters;

import org.example.app.loggers.MyFormatter;

import javax.servlet.*;
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
import static org.example.app.filters.FilterData.*;
import static org.example.app.data.DataURL.*;

@WebFilter("/email/main")
public class AuthenticationFilter implements Filter {

    public final static Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new MyFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("Response received to /email/main (WebFilter)");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpURLConnection connection = getConnection();
        logger.info("Connections is set with authentication server");
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
            logger.info("Response is send to controller");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            logger.log(Level.WARNING, "User isn't authorized! Response is send to", EMAIL);
            response.sendRedirect(EMAIL);
        }
    }

    private int getCorrectStatus() {
        return SC_OK;
    }

    private void sendData(HttpURLConnection connection, HttpServletRequest request) throws IOException {
        connection.setRequestProperty(COOKIE, request.getHeader(COOKIE));
        connection.connect();
        logger.info("Data (with cookie) are send to authentication server");
    }

    private HttpURLConnection getConnection() throws IOException {
        String urlString = AUTHENTICATION_SERVER + AUTHENTICATION;
        logger.log(Level.INFO, "Connection is setting with authenticate server: ", urlString);
        URL url = new URL(urlString);
        return (HttpURLConnection) url.openConnection();
    }
}
