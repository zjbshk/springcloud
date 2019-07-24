package top.itreatment.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.itreatment.beans.Student;

@FeignClient("SERVICE-PROVIDER")
public interface HelloService {

    @GetMapping("/hello")
    String index();

    @GetMapping("/getstu/{id}")
    Student getStudent(@PathVariable("id") String name);
}
