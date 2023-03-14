package com.dp.spring.parallel.common.exceptions;

public enum ExceptionConstants {
    WRONG_CREDENTIALS("BAD-101", "FAILED AUTHENTICATION", "Wrong credentials."),
    TOKEN_NOT_VALID("BAD-102", "FAILED AUTHENTICATION", "Please login to open a new session."),
    EMAIL_NOT_FOUND("BAD-103", "USER NOT FOUND", "Couldn't find user for email %s"),

    DEVICE_NOT_FOUND("XXXX", "DEVICE NOT FOUND", "Couldn't find device for id %s"),
    NODE_NOT_FOUND("XXXY", "NODE NOT FOUND", "Couldn't find node");

    private final String code;
    private final String title;
    private final String detail;

    ExceptionConstants(final String code, final String title, final String detail) {
        this.code = code;
        this.title = title;
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }
}
