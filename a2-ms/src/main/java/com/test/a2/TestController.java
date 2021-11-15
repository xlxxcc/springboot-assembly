package com.test.a2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@Slf4j
public class TestController {

    @Value("${public.p1}")
    private String publicA1;

    @Value("${public.p1}")
    private String publicA2;

    @Value("${private.a3:null}")
    private String privateA3;

    @Value("${private.a4}")
    private String privateA4;

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/a2")
    public String a1() {
        return "a2";
    }

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @PostConstruct
    public void log() {
        log.info("application-a1, publicA1: {}, publicA2: {}, privateA3: {}, privateA4: {}, appName: {}, configurableApplicationContext: {}",
                publicA1, publicA2, privateA3, privateA4, appName, configurableApplicationContext);
    }
}
