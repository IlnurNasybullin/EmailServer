package org.example.app.filters;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
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
    protected int getCorrectStatus() {
        return SC_CREATED;
    }

    protected void sendData(HttpURLConnection connection, HttpServletRequest request) throws IOException {
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        byte[] bytes = request.getInputStream().readAllBytes();
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        connection.connect();
    }

    protected HttpURLConnection getConnection(HttpServletRequest request) throws IOException {
        URL url = new URL(AUTHENTICATION_SERVER + REGISTRATION);
        return (HttpURLConnection) url.openConnection();
    }
}
