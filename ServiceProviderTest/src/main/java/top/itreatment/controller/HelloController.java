package top.itreatment.controller;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.itreatment.beans.Student;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class HelloController {

    @Autowired
    Registration registration;

    static Map<String, Student> studentMap = new ConcurrentHashMap<>();

    static {
        studentMap.put("张三", new Student("张三", 18));
        studentMap.put("李四", new Student("李四", 19));
        studentMap.put("王五", new Student("王五", 21));
    }

    @GetMapping("/hello")
    public String hello() {
        String host = registration.getHost();
        int port = registration.getPort();
        return String.format("Host:%s,Port:%d", host, port);
    }

    @GetMapping("/getstu/{name}")
    public Student getStudent(@PathVariable("name") String name) {
        return studentMap.get(name);
    }
}
