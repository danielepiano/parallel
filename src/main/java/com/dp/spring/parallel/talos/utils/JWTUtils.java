package com.dp.spring.parallel.talos.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTUtils {
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static String SECRET_KEY;
    public static long EXPIRES_IN_MILLIS;

    @Value("${jwt.secret}")
    private void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    @Value("${jwt.expires-in-millis}")
    private void setExpiresInMillis(long expiresInMillis) {
        EXPIRES_IN_MILLIS = expiresInMillis;
    }
}