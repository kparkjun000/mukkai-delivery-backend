package org.delivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
    
    @GetMapping("/health")
    public String health() {
        return "redirect:/open-api/health";
    }
}
