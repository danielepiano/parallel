package com.dp.spring.parallel.hestia.database.enums;

public enum UserPermission {
    ADMIN_USER_READ("admin-user:read"),
    ADMIN_USER_WRITE("admin-user:write"),

    COMPANY_MANAGER_USER_READ("company-manager-user:read"),
    COMPANY_MANAGER_USER_WRITE("company-manager-user:write"),

    HEADQUARTERS_RECEPTIONIST_USER_READ("headquarters-receptionist-user:read"),
    HEADQUARTERS_RECEPTIONIST_USER_WRITE("headquarters-receptionist-user:write"),

    EMPLOYEE_USER_READ("employee-user:read"),
    EMPLOYEE_USER_WRITE("employee-user:write"),


    RESOURCEX_READ("x:read"),
    RESOURCEY_WRITE("x:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }
}
