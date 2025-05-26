package com.example.ms2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ms2")
public class Ms2Controller {

    @GetMapping("/test")
    public String test() {
        return "ms2 hello upgrade";
    }
}