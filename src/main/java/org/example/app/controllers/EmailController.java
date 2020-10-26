package org.example.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("email")
public class EmailController {

    @GetMapping
    public String signInForm(@RequestParam(defaultValue = "true") boolean isCorrect) {
        return "sign";
    }

    @GetMapping("registration_form")
    public String registrationForm(@RequestParam(defaultValue = "true") boolean isCorrect) {
        return "registration";
    }

    @GetMapping("sign")
    public void sign(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:9001/email/main");
    }

    @PostMapping("registration")
    public void register(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:9001/email/main");
    }

    @GetMapping("main")
    public String main() {
        return "index";
    }

}
