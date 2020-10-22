package org.example.app;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.servlet.http.Cookie;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        String key = "s";
        String value = "ss";

        Cookie cookie = new Cookie(key, value);
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = mapper.writeValueAsBytes(cookie);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Cookie.class, new JsonDeserializer<Cookie>() {
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

                Cookie cook = new Cookie(name, value);
                cook.setMaxAge(maxAge);
                cook.setHttpOnly(httpOnly);
                cook.setComment(comment);
                cook.setDomain(domain);
                cook.setPath(path);
                cook.setSecure(secure);
                cook.setVersion(version);

                return cook;
            }
        });
        mapper.registerModule(module);
        Cookie a = mapper.readValue(bytes, Cookie.class);
        System.out.println(a.getValue());
    }
}
