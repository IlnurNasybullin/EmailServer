package org.example.app.controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.app.models.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/email")
public class AuthenticationController {

    private static class CookieDeserializer extends JsonDeserializer<Cookie> {

        @Override
        public Cookie deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);

            String name = node.get("name").asText();
            String value = node.get("value").asText();
            int version = node.get("version").asInt();
            String comment = node.get("comment").asText();
            String domain = node.get("domain").asText();
            int maxAge = node.get("maxAge").asInt();
            String path = node.get("path").asText();
            boolean secure = node.get("secure").asBoolean();
            boolean httpOnly = node.get("httpOnly").asBoolean();

            Cookie cookie = new Cookie(name, value);
            cookie.setMaxAge(maxAge);
            cookie.setHttpOnly(httpOnly);
            cookie.setComment(comment);
            cookie.setDomain(domain);
            cookie.setPath(path);
            cookie.setSecure(secure);
            cookie.setVersion(version);

            return cookie;
        }
    }

    private static final String authenticationServer;
    private static final String REGISTRATION = "registration";
    public static final String SIGN = "sign";
    private static final String AUTHENTICATION = "authentication";

    static {
        authenticationServer = "http://localhost:9000/";
    }

    @PostMapping("/registration")
    public void registry(@RequestBody User user, HttpServletResponse response) throws IOException {
        HttpURLConnection connection = getConnection(authenticationServer + REGISTRATION);
        int code = sendUser(connection, user);
        response.setStatus(code);

        if (code == HttpServletResponse.SC_OK) {
            setCookie(response, connection);
        }
    }

    private void setCookie(HttpServletResponse response, HttpURLConnection connection) throws IOException {
        String cookieValue = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                .lines().collect(Collectors.joining());
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Cookie.class, new CookieDeserializer());

        mapper.registerModule(module);
        response.addCookie(mapper.readValue(cookieValue, Cookie.class));
    }

    private HttpURLConnection getConnection(String stringURL) throws IOException {
        URL url = new URL(stringURL);
        return (HttpURLConnection) url.openConnection();
    }

    @GetMapping("/sign")
    public void sign(@RequestParam String email, @RequestParam String password, HttpServletResponse response) throws IOException {
        String query = String.format("?email=%s&password=%s", email, password);
        HttpURLConnection connection = getConnection(authenticationServer + SIGN + query);
        int code = sendEmailAndPassword(connection, email, password);
        response.setStatus(code);

        if (code == HttpServletResponse.SC_OK) {
            setCookie(response, connection);
        }
    }

    @GetMapping("/something")
    public void something(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int code = checkOnAuthentication(request);
        response.setStatus(code);
    }

    private int checkOnAuthentication(HttpServletRequest request) throws IOException {
        HttpURLConnection connection = getConnection(authenticationServer + SIGN);
        connection.connect();
        return connection.getResponseCode();
    }

    private int sendEmailAndPassword(HttpURLConnection connection, String email, String password) throws IOException {
        connection.setDoOutput(false);
        connection.connect();
        return connection.getResponseCode();
    }

    private int sendUser(HttpURLConnection connection, User user) throws IOException {
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(connection.getOutputStream(), user);
        connection.getOutputStream().flush();
        connection.connect();
        return connection.getResponseCode();
    }
}
