package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceBController {

    @GetMapping("/service-b")
    public String handleRequest() {
        return "Response from Service B";
    }
}
