package org.example.app.filters;

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

import static javax.servlet.http.HttpServletResponse.SC_OK;

@WebFilter(value = "/email/sign")
public class SignInFilter extends AuthorizationFilter {

    private int getCorrectStatus() {
        return SC_OK;
    }

    private void sendData(HttpURLConnection connection, HttpServletRequest request) throws IOException {
        connection.connect();
    }

    private HttpURLConnection getConnection(HttpServletRequest request) throws IOException {
        URL url = new URL(FilterData.AUTHENTICATION_SERVER + FilterData.SIGN_IN + "?" + request.getQueryString());
        return (HttpURLConnection) url.openConnection();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpURLConnection connection = getConnection(request);
        sendData(connection, request);

        int code = connection.getResponseCode();
        if (code == getCorrectStatus()) {
            setCookie(connection, response);
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect("http://localhost:9001/email");
        }
    }
}
