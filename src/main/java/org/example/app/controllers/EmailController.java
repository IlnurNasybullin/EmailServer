package org.example.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/email")
public class EmailController {

    @GetMapping
    public String form() {
        return "sign";
    }

    @GetMapping("/something")
    public void something() {
        System.out.println("something");
    }

}
