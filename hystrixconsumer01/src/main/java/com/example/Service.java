package com.example;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "provide", fallback = MyService.class)
public interface Service {

    @GetMapping("hello")
    String getInfoByProvide();
}
