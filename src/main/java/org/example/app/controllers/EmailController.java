package org.example.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/email")
public class EmailController {

    @GetMapping
    public String form() {
        return "sign";
    }

    @GetMapping("sign")
    public void sign(@RequestParam String email, @RequestParam String password, HttpServletResponse response) throws IOException {
        System.out.println(response.getHeader("Set-Cookie"));
        response.sendRedirect("http://localhost:9001/email/main");
    }

    @GetMapping("/main")
    public String main() {
        System.out.println("main");
        return "index";
    }

}
