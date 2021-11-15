package com.test.a1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Configuration
@RestController
@Slf4j
public class TestController {

    @Value("${public.p1}")
    private String publicA1;

    @Value("${public.p1}")
    private String publicA2;

    @Value("${private.a1}")
    private String privateA1;

    @Value("${private.a2}")
    private String privateA2;

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/a1")
    public String a1() {
        return "a1";
    }

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @PostConstruct
    public void log() {
        log.info("application-a1, publicA1: {}, publicA2: {}, privateA1: {}, privateA2: {}, appName: {}, configurableApplicationContext: {}",
                publicA1, publicA2, privateA1, privateA2, appName, configurableApplicationContext);
    }

}
