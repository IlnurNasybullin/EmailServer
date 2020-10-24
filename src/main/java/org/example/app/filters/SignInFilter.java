package org.example.app.filters;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@WebFilter(value = "/email/sign", initParams = @WebInitParam(name = "email", value = "email"))
public class SignInFilter extends AbstractFilter {
    @Override
    protected int getCorrectStatus() {
        return SC_OK;
    }

    @Override
    protected void sendData(HttpURLConnection connection, HttpServletRequest request) throws IOException {
        connection.connect();
    }

    @Override
    protected HttpURLConnection getConnection(HttpServletRequest request) throws IOException {
        URL url = new URL(FilterData.AUTHENTICATION_SERVER + FilterData.SIGN_IN + "?" + request.getQueryString());
        return (HttpURLConnection) url.openConnection();
    }
}
