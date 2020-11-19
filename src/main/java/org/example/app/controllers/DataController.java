package org.example.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.app.loggers.MyFormatter;
import org.example.app.models.User;
import org.example.app.repositories.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.app.data.DataURL.EMAIL;
import static org.example.app.filters.FilterData.*;

@Controller
@RequestMapping("email")
public class DataController {

    public final static Logger logger = Logger.getLogger(DataController.class.getName());

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new MyFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    @Autowired
    @Qualifier("userRepository")
    private Repository<User> userRepository;

    @GetMapping("getUser")
    public void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("Response for getting personal data of user");
        String email = getEmail(request, response);

        ObjectMapper mapper = new ObjectMapper();

        User user = userRepository.select(email);
        mapper.writeValue(response.getOutputStream(), user);
        logger.info("User data are send");
    }

    private String getEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String urlString = AUTHENTICATION_SERVER + GET_EMAIL;
        logger.log(Level.INFO, "Connection is setting with authenticate server: ", urlString);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty(COOKIE, request.getHeader(COOKIE));
        connection.connect();
        logger.info("Data (with cookie) are send to authentication server");

        int code = connection.getResponseCode();
        logger.log(Level.INFO, "Response code - ", code);
        if (code == HttpServletResponse.SC_OK) {
            String email = new String(connection.getInputStream().readAllBytes());
            logger.log(Level.INFO, "Email is get - ", email);
            return email;
        } else {
            logger.warning("Redirect to sign in menu");
            response.sendRedirect(EMAIL);
        }

        return null;
    }

    @PostMapping("/saveUser")
    public void save(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("Response for saving personal data of user");
        String email = getEmail(request, response);
        user.setEmail(email);
        if (userRepository.update(user)) {
            logger.info("User data is updated in database");
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            logger.warning("User data isn't updated in database");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
