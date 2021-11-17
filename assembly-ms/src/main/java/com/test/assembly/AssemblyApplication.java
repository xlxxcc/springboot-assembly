package com.test.assembly;

import com.test.a1.A1Application;
import com.test.a2.A2Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.test.assembly")
public class AssemblyApplication {

    public static void main(String[] args) {
        System.setProperty("management.endpoints.web.exposure.include", "*");
        //System.setProperty("spring.cloud.service-registry.auto-registration.enabled", "false");
        final ConfigurableApplicationContext commonContext =
                new SpringApplicationBuilder(AssemblyApplication.class).web(WebApplicationType.NONE).run(args);
        log.info(commonContext.getId() + " isActive: " + commonContext.isActive());
        log.info(commonContext.getId() + " env: " + commonContext.getEnvironment().toString());

        //System.setProperty("spring.cloud.service-registry.auto-registration.enabled", "true");
//        System.setProperty("spring.cloud.consul.discovery.instance-id", "${spring.application.name}-${spring.cloud.client.ip-address}-${server.port}");

        // a1
        if (commonContext.getEnvironment().containsProperty("a1")) {
            final ConfigurableApplicationContext context =
                    new SpringApplicationBuilder(A1Application.class)
                            .parent(commonContext)
                            .properties("server.port=9060")
                            .properties("spring.application.name=a1-ms")
                            .properties("spring.cloud.consul.discovery.instance-id=a1")
//                            .properties("spring.cloud.consul.service-registry.auto-registration.enabled=true")
                            .sources(RefreshScope.class)
                            .profiles("additional")
                            .run(args);
            log.info(context.getId() + " isActive: " + context.isActive());
            log.info(commonContext.getId() + " env: " + context.getEnvironment().toString());
        }

        // a2
        if (commonContext.getEnvironment().containsProperty("a2")) {
            final ConfigurableApplicationContext context =
                    new SpringApplicationBuilder(A2Application.class)
                            .parent(commonContext)
                            .properties("server.port=9070")
                            .properties("spring.application.name=a2-ms")
                            .properties("spring.cloud.consul.discovery.instance-id=a2")
                            .sources(RefreshScope.class)
                            .run(args);
            log.info(context.getId() + " isActive: " + context.isActive());
            log.info(commonContext.getId() + " env: " + context.getEnvironment().toString());
        }
    }
}
