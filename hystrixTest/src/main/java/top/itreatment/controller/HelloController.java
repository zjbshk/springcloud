package top.itreatment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.itreatment.service.HelloService;

@RestController
public class HelloController {

    @Autowired
    HelloService service;

    @GetMapping("/hello")
    String index() {
        return service.index();
    }
}
