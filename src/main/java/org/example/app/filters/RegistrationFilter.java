package org.example.app.filters;

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

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static org.example.app.filters.FilterData.AUTHENTICATION_SERVER;
import static org.example.app.filters.FilterData.REGISTRATION;

@WebFilter("/email/registration")
public class RegistrationFilter extends CookieFilter {

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
            response.sendRedirect("http://localhost:9001/email/registration_form");
        }
    }

    private int getCorrectStatus() {
        return SC_CREATED;
    }

    private void sendData(HttpURLConnection connection, HttpServletRequest request) throws IOException {
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        byte[] bytes = request.getInputStream().readAllBytes();

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        connection.connect();
    }

    private HttpURLConnection getConnection(HttpServletRequest request) throws IOException {
        URL url = new URL(AUTHENTICATION_SERVER + REGISTRATION);
        return (HttpURLConnection) url.openConnection();
    }
}
