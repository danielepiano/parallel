package com.dp.spring.parallel.hestia.database.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public enum UserRole {
    ADMIN(Constants.ROLE_ADMIN_VALUE),
    COMPANY_MANAGER(Constants.ROLE_COMPANY_MANAGER_VALUE),
    HEADQUARTERS_RECEPTIONIST(Constants.ROLE_HEADQUARTERS_RECEPTIONIST_VALUE),
    EMPLOYEE(Constants.ROLE_EMPLOYEE_VALUE),

    UNSET(Constants.ROLE_UNSET_VALUE);


    private final String role;

    @JsonCreator
    UserRole(final String role) {
        this.role = role;
    }

    @JsonValue
    public String getRole() {
        return this.role;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return Set.of(new SimpleGrantedAuthority(this.role));
    }


    public static class Constants {
        private static final String ROLE_PREFIX = "ROLE_";

        public static final String ADMIN_VALUE = "ADMIN";
        public static final String COMPANY_MANAGER_VALUE = "COMPANY_MANAGER";
        public static final String HEADQUARTERS_RECEPTIONIST_VALUE = "HEADQUARTERS_RECEPTIONIST";
        public static final String EMPLOYEE_VALUE = "EMPLOYEE";

        public static final String UNSET_VALUE = "UNSET";

        public static final String ROLE_ADMIN_VALUE = ROLE_PREFIX + ADMIN_VALUE;
        public static final String ROLE_COMPANY_MANAGER_VALUE = ROLE_PREFIX + COMPANY_MANAGER_VALUE;
        public static final String ROLE_HEADQUARTERS_RECEPTIONIST_VALUE = ROLE_PREFIX + HEADQUARTERS_RECEPTIONIST_VALUE;
        public static final String ROLE_EMPLOYEE_VALUE = ROLE_PREFIX + EMPLOYEE_VALUE;
        public static final String ROLE_UNSET_VALUE = ROLE_PREFIX + UNSET_VALUE;
    }
}
