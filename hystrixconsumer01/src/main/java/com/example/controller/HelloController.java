package com.example.controller;


import com.example.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    Service service;

    @GetMapping("/index")
    public String index() {
        return service.getInfoByProvide();
    }
}
