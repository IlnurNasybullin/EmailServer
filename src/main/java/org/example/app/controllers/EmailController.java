package org.example.app.controllers;

import org.example.app.loggers.MyFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.app.data.DataURL.*;

@Controller
@RequestMapping("email")
public class EmailController {

    public final static Logger logger = Logger.getLogger(EmailController.class.getName());

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new MyFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    @GetMapping
    public String signInForm(@RequestParam(defaultValue = "true") boolean isCorrect) {
        logger.log(Level.INFO, "Correcting sing in form:", isCorrect);
        return "sign";
    }

    @GetMapping("registration_form")
    public String registrationForm(@RequestParam(defaultValue = "true") boolean isCorrect) {
        logger.log(Level.INFO, "Correcting registration form:", isCorrect);
        return "registration";
    }

    @GetMapping("sign")
    public void sign(HttpServletResponse response) throws IOException {
        logger.info("Response on sign in system");
        response.sendRedirect(EMAIL_MAIN);
        logger.log(Level.INFO, "Response received to", EMAIL_MAIN);
    }

    @PostMapping("registration")
    public void register(HttpServletResponse response) throws IOException {
        logger.info("Response on registration in system");
        response.sendRedirect(EMAIL_MAIN);
        logger.log(Level.INFO, "Response received to", EMAIL_MAIN);
    }

    @GetMapping("main")
    public String main() {
        logger.info("Response on main page in system");
        return "main";
    }

    @GetMapping("exit")
    public void exit(HttpServletResponse response) throws IOException {
        logger.info("Response on sign in system");
        response.sendRedirect(EMAIL);
        logger.log(Level.INFO, "Response received to", EMAIL);
    }
}
