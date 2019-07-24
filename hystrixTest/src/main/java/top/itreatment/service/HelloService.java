package top.itreatment.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloFallback")
    public String index() {
        String body = restTemplate.getForEntity("http://SERVICE-PROVIDER/hello", String.class).getBody();
        return body;
    }

    public String helloFallback() {
        return "服务器正在升级，请稍后重试......";
    }
}
