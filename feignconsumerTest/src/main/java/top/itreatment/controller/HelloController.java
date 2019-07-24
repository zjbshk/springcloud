package top.itreatment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.itreatment.beans.Student;
import top.itreatment.service.HelloService;

@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String index() {
        return helloService.index();
    }

    @GetMapping("/getstu/{name}")
    public Student getStudent(@PathVariable("name") String name){
        return helloService.getStudent(name);
    }
}
