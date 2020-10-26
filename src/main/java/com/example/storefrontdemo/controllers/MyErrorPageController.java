package com.example.storefrontdemo.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class MyErrorPageController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        //do something like logging
        return "myerror/show";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}

