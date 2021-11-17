package com.test.a1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationAutoConfiguration;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationListener;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "unused", "serial", "CollectionAddAllCanBeReplacedWithConstructor", "unchecked"})
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
    private ApplicationContext applicationContext;

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Autowired
    private AnnotationConfigServletWebServerApplicationContext annotationConfigServletWebServerApplicationContext;

    @Autowired(required = false)
    private RefreshScope refreshScope;

    @PostConstruct
    public void log() {
        log.info("application-a1, publicA1: {}, publicA2: {}, privateA1: {}, privateA2: {}, appName: {}, configurableApplicationContext: {}",
                publicA1, publicA2, privateA1, privateA2, appName, configurableApplicationContext);

        try {
            applicationContext.getBean(AutoServiceRegistrationProperties.class);
            log.info("AutoServiceRegistrationProperties 已经注入");
        } catch (final Exception e) {
            log.info("AutoServiceRegistrationProperties 未被注入");
        }

        final String key = "spring.cloud.service-registry.auto-registration.enabled";
        log.info("{}={}", key, applicationContext.getEnvironment().getProperty(key));

        // 动态修改配置
        ConfigurableEnvironment environment = (ConfigurableEnvironment) applicationContext.getEnvironment();
        String name = "Config resource 'class path resource [application.yaml]' via location 'optional:classpath:/'";
        OriginTrackedMapPropertySource propertySource = (OriginTrackedMapPropertySource) environment.getPropertySources().get(name);
        @SuppressWarnings("ConstantConditions")
        Map<String, Object> source = propertySource.getSource();
        Map map = new HashMap();
        map.putAll(source);
        map.put(key, "true");
        environment.getPropertySources().replace(name, new OriginTrackedMapPropertySource(name, map));

        applicationContext.publishEvent(new EnvironmentChangeEvent(new HashSet<String>(){{add(key);}}));
        // 如果需要更新spring cloud中的一些组件属性，如zuul、Eureka，还需要执行此方法
//         refreshScope.refreshAll();

//        configurableApplicationContext.refresh();

        // 动态注册bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(AutoServiceRegistrationProperties.class);
        ((DefaultListableBeanFactory)configurableApplicationContext.getBeanFactory()).registerBeanDefinition("autoServiceRegistrationProperties", beanDefinitionBuilder.getRawBeanDefinition());
        configurableApplicationContext.getBeanFactory().configureBean(AutoServiceRegistrationProperties.class, "autoServiceRegistrationProperties");
        log.info("{}={}", key, applicationContext.getEnvironment().getProperty(key));
        try {
            // getBean触发bean实例化
            applicationContext.getBean(AutoServiceRegistrationProperties.class);
            log.info("AutoServiceRegistrationProperties 已经注入");
        } catch (final Exception e) {
            log.info("AutoServiceRegistrationProperties 未被注入");
        }

        // 动态注册@Configuraton类里的bean, 通过ConfigurationClassPostProcessor扫描加载@Configuraton注解类的bean定义
        BeanDefinitionBuilder beanDefinitionBuilder1 = BeanDefinitionBuilder.genericBeanDefinition(ConsulAutoServiceRegistrationAutoConfiguration.class);
        ((DefaultListableBeanFactory)configurableApplicationContext.getBeanFactory()).registerBeanDefinition("consulAutoServiceRegistrationAutoConfiguration", beanDefinitionBuilder1.getRawBeanDefinition());
        configurableApplicationContext.getBean(ConfigurationClassPostProcessor.class).processConfigBeanDefinitions(annotationConfigServletWebServerApplicationContext);
        try {
            // getBean触发bean实例化
            applicationContext.getBean(ConsulAutoServiceRegistrationListener.class);
            applicationContext.getBean(ConsulAutoRegistration.class);
            log.info("AutoServiceRegistrationProperties 已经注入");
        } catch (final Exception e) {
            log.info("AutoServiceRegistrationProperties 未被注入");
        }
    }

}
