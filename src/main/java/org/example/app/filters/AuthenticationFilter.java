package org.example.app.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@WebFilter("/email/main")
public class AuthenticationFilter implements Filter {

    public static final String COOKIE = "Cookie";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpURLConnection connection = getConnection();

        try {
            sendData(connection, request);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("http://localhost:9001/" + 500);
            return;
        }

        int code = connection.getResponseCode();

        if (code == getCorrectStatus()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            response.sendRedirect("http://localhost:9001/email");
        }
    }

    private int getCorrectStatus() {
        return SC_OK;
    }

    private void sendData(HttpURLConnection connection, HttpServletRequest request) throws IOException {
        connection.setRequestProperty(COOKIE, request.getHeader(COOKIE));
        connection.connect();
    }

    private HttpURLConnection getConnection() throws IOException {
        URL url = new URL(FilterData.AUTHENTICATION_SERVER + FilterData.AUTHENTICATION);
        return (HttpURLConnection) url.openConnection();
    }
}
