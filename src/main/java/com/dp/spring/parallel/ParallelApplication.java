package com.dp.spring.parallel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication(scanBasePackages = "com.dp.spring")
@EnableFeignClients(basePackages = "com.dp.spring")
@EntityScan("com.dp.spring")
@EnableJpaRepositories("com.dp.spring")
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@EnableAsync
public class ParallelApplication {
    public static void main(String[] args) {
        final String version = ParallelApplication.class.getPackage().getImplementationVersion();
        final String title = ParallelApplication.class.getPackage().getImplementationTitle();
        final String vendor = ParallelApplication.class.getPackage().getImplementationVendor();

        if (version != null) {
            System.setProperty("build.version", version);
        }
        if (title != null) {
            System.setProperty("build.title", title);
        }
        if (vendor != null) {
            System.setProperty("build.vendor", vendor);
        }

        SpringApplication.run(ParallelApplication.class, args);
    }
}
