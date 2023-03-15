package com.dp.spring.parallel.hestia.database.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public enum UserRole {
    ADMIN(Constants.ADMIN_VALUE),
    COMPANY_MANAGER(Constants.COMPANY_MANAGER_VALUE),
    HEADQUARTERS_RECEPTIONIST(Constants.HEADQUARTERS_RECEPTIONIST_VALUE),
    EMPLOYEE(Constants.EMPLOYEE_VALUE);


    private final String role;

    UserRole(final String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return Set.of(new SimpleGrantedAuthority(this.role));
    }


    public class Constants {
        private static final String ROLE_PREFIX = "ROLE_";

        public static final String ADMIN_VALUE = ROLE_PREFIX + "ADMIN";
        public static final String COMPANY_MANAGER_VALUE = ROLE_PREFIX + "COMPANY_MANAGER";
        public static final String HEADQUARTERS_RECEPTIONIST_VALUE = ROLE_PREFIX + "HEADQUARTERS_RECEPTIONIST";
        public static final String EMPLOYEE_VALUE = ROLE_PREFIX + "EMPLOYEE";
    }
}
