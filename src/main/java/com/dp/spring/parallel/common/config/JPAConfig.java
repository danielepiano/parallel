package com.dp.spring.parallel.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "jwtAuditorAware")
public class JPAConfig {

    /**
     * @return the username of whoever touch a database record, in order to set createdBy and lastModifiedBy fields
     */
    @Bean
    public AuditorAware<String> jwtAuditorAware() {
        return () -> {
            /*Optional.ofNullable( SecurityContextHolder.getContext() )
                    .map( SecurityContext::getAuthentication )
                    .filter( Authentication::isAuthenticated )
                    .map( Authentication::getPrincipal )
                    .map( UserDetails.class::cast )
                    .map( UserDetails::getUsername );*/

            var authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            var userDetails = authentication.getPrincipal();
            if (userDetails instanceof UserDetails) {
                return Optional.of(((UserDetails) userDetails).getUsername());
            } else {
                return Optional.empty();
            }
        };
    }
}
