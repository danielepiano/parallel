package com.dp.spring.parallel.hestia.config;

import com.dp.spring.parallel.hestia.filters.JWTAuthenticationExceptionResolverFilter;
import com.dp.spring.parallel.hestia.filters.JWTAuthenticationFilter;
import com.dp.spring.parallel.hestia.services.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationExceptionResolverFilter jwtAuthenticationExceptionResolverFilter;
    private final JWTAuthenticationFilter jwtAuthFilter;
    private final LogoutService logoutHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authenticationProvider( this.authenticationProvider )
                .addFilterBefore(this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.jwtAuthenticationExceptionResolverFilter, JWTAuthenticationFilter.class)
                .logout()
                    .logoutUrl(LogoutService.LOGOUT_URL)
                    .addLogoutHandler(this.logoutHandler)
                    .logoutSuccessHandler( (request, response, authentication) -> SecurityContextHolder.clearContext() );

        return httpSecurity.build();
    }
}
