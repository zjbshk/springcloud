package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("native")
public class GetConfig {

    @Value("${foo}")
    private String foo;

    @Value("${server.port}")
    private int port;

    @GetMapping("getconfig")
    public String index() {
        return String.format("foo:%s , port:%d", foo, port);
    }
}
