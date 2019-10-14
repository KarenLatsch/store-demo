package com.example.storefrontdemo.controllers;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@ComponentScan(basePackages = "package com.example.storefrontdemo.controllers;")

public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
