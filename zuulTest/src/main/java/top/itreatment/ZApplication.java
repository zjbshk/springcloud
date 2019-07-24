package top.itreatment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import top.itreatment.filter.AccessFilter;

@SpringBootApplication
@EnableZuulProxy
public class ZApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZApplication.class, args);
    }

    @Bean
    AccessFilter accessFilter(){
        return new AccessFilter();
    }

    @Bean
    PatternServiceRouteMapper serviceRouteMapper(){
        return new PatternServiceRouteMapper("(?<name>^.+?)-(?<action>.+?)","${name}/${action}");
    }
}
