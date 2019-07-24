package top.itreatment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FCApplication {

    public static void main(String[] args) {
        SpringApplication.run(FCApplication.class, args);
    }
}
