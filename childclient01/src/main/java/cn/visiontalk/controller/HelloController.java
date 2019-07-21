package cn.visiontalk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    Registration registration;

    @GetMapping("/hello")
    public String hello() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println("service = " + service);
        }

        String host = registration.getHost();
        int port = registration.getPort();

//        只是在这个地方做了小量更改，为了知道是那个提供了服务
        String format = String.format("Host:%s,Port:%d\t------>hello world", host, port);
        return format;
    }

}
