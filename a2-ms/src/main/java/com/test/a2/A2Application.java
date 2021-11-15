package com.test.a2;

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
@PropertySource(value = {"classpath:a2.properties"})
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {A2Application.class})
public class A2Application {

    public static void main(String[] args) {
        SpringApplication.run(A2Application.class, args);
    }

}
