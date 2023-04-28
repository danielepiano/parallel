package com.dp.spring.parallel.talos.config;

import com.dp.spring.parallel.common.exceptions.EmailNotFoundException;
import com.dp.spring.parallel.hestia.database.entities.*;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {
    private final UserRepository<User> userRepository;
    private final UserRepository<AdminUser> adminUserRepository;
    private final UserRepository<CompanyManagerUser> companyManagerUserRepository;
    private final UserRepository<HeadquartersReceptionistUser> headquartersReceptionistUserRepository;
    private final UserRepository<EmployeeUser> employeeUserRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            final User authenticated = this.userRepository.findByEmail(username)
                    .orElseThrow(() -> new EmailNotFoundException(username));

            return switch (authenticated.getRole()) {
                case ADMIN -> this.adminUserRepository.getReferenceById(authenticated.getId());
                case COMPANY_MANAGER -> this.companyManagerUserRepository.getReferenceById(authenticated.getId());
                case HEADQUARTERS_RECEPTIONIST ->
                        this.headquartersReceptionistUserRepository.getReferenceById(authenticated.getId());
                case EMPLOYEE -> this.employeeUserRepository.getReferenceById(authenticated.getId());
                default -> authenticated;
            };
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
