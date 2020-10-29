package org.example.app.controllers;

import org.example.app.App;
import org.example.app.loggers.MyFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/error")
public class BadStatusesController {

    public final static Logger logger = Logger.getLogger(BadStatusesController.class.getName());

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new MyFormatter());
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    @GetMapping("/{status}")
    public String getBadPage(@PathVariable String status) {
        logger.log(Level.SEVERE, "Response received with status", status);
        return "badPage";
    }

}
