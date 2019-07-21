package com.example;

import org.springframework.stereotype.Component;

@Component
public class MyService implements Service {
    @Override
    public String getInfoByProvide() {
        return "服务器正在维护，请稍后重试......";
    }
}
