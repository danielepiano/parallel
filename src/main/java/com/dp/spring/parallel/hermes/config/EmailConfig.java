package com.dp.spring.parallel.hermes.config;

import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class EmailConfig {
    public static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();
    public static final String FROM = "no-reply@parallel.com";
}
