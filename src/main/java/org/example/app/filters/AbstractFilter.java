package org.example.app.filters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;

public abstract class AbstractFilter implements Filter {

    private final static JsonDeserializer<Cookie> cookieDeserializer;

    static {
        cookieDeserializer = new JsonDeserializer<>() {
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
        };
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
        }
        response.setStatus(code);
    }

    protected abstract int getCorrectStatus();

    protected abstract void sendData(HttpURLConnection connection, HttpServletRequest request) throws IOException;

    protected abstract HttpURLConnection getConnection(HttpServletRequest request) throws IOException;

    private void setCookie(HttpURLConnection connection, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = getMapper();
        Cookie cookie = mapper.readValue(connection.getInputStream(), Cookie.class);
        response.addCookie(cookie);
    }

    private ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Cookie.class, cookieDeserializer);
        mapper.registerModule(module);

        return mapper;
    }
}
