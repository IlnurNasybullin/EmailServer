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
public class AuthenticationFilter extends AbstractFilter {

    public static final String COOKIE = "Cookie";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpURLConnection connection = getConnection(request);
        sendData(connection, request);

        int code = connection.getResponseCode();
        System.out.println(code);
        if (code == getCorrectStatus()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            response.setStatus(code);
        }
    }

    @Override
    protected int getCorrectStatus() {
        return SC_OK;
    }

    @Override
    protected void sendData(HttpURLConnection connection, HttpServletRequest request) throws IOException {
        connection.setRequestProperty(COOKIE, request.getHeader(COOKIE));
        connection.connect();
    }

    @Override
    protected HttpURLConnection getConnection(HttpServletRequest request) throws IOException {
        URL url = new URL(FilterData.AUTHENTICATION_SERVER + FilterData.AUTHENTICATION);
        return (HttpURLConnection) url.openConnection();
    }
}
