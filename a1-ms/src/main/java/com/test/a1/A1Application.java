package com.test.a1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@EnableDiscoveryClient
@EnableAspectJAutoProxy
@Configuration
@PropertySource(value = {"classpath:a1.properties"})
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {A1Application.class})
public class A1Application {

    public static void main(String[] args) {
        SpringApplication.run(A1Application.class, args);
    }

}

