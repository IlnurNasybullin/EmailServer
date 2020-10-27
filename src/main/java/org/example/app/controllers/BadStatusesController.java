package org.example.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class BadStatusesController {

    @GetMapping("{status}")
    public String getBadPage(@PathVariable int status) {
        System.out.println(status);
        return "badPage";
    }

}
