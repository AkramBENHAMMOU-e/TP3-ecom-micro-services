package com.tp.customerservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ConfigTestController{
    @Value("${global.params.p1}")
    private  String a;
    @Value("${global.params.p2}")
    private  String b;

    @GetMapping("/testConfig1")
    public Map<String,String> configTest(){
        return Map.of("p1",a,"p2",b);
    }
}
