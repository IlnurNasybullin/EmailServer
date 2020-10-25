package org.example.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("email")
public class EmailController {

    @GetMapping
    public String form(HttpServletRequest request) {
        return "sign";
    }

    @GetMapping("sign")
    public void sign(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:9001/email/main");
    }

    @GetMapping("main")
    public String main() {
        return "index";
    }

}
