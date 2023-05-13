package com.dp.spring.parallel.common.exceptions;

public enum _ExceptionConstants {
    WRONG_CREDENTIALS("BAD-101", "FAILED AUTHENTICATION", "Wrong credentials."),
    TOKEN_NOT_VALID("BAD-102", "FAILED AUTHENTICATION", "Please login to open a new session."),
    EMAIL_NOT_FOUND("BAD-103", "USER NOT FOUND", "Couldn't find user for email %s."),
    HEADQUARTERS_NOT_FOUND("BAD-104", "HEADQUARTERS NOT FOUND", "Couldn't find headquarters for id %s."),
    COMPANY_NOT_FOUND("BAD-105", "COMPANY NOT FOUND", "Couldn't find company for id %s."),
    EMAIL_ALREADY_EXISTS("BAD-106", "EMAIL ALREADY EXISTS", "Couldn't create the user: email %s associated to an existing account."),
    USER_NOT_FOUND("BAD-107", "USER NOT FOUND", "Couldn't find %s user for id %s."),
    COMPANY_ALREADY_EXISTS("BAD-108", "COMPANY ALREADY EXISTS", "Couldn't complete the operation: the company '%s' located in %s, %s already exists."),
    HEADQUARTERS_ALREADY_EXISTS("BAD-109", "HEADQUARTERS ALREADY EXISTS", "Couldn't complete the operation: a headquarters located in %s, %s already exists for this company."),
    HEADQUARTERS_NOT_DELETABLE("BAD-110", "HEADQUARTERS NOT DELETABLE", "Couldn't complete the operation: some resource is still attached to the headquarters %s."),
    COMPANY_NOT_DELETABLE("BAD-111", "COMPANY NOT DELETABLE", "Couldn't complete the operation: some resource is still attached to the company %s."),
    USER_NOT_DELETABLE("BAD-112", "USER NOT DELETABLE", "Couldn't complete the operation%s."),


    FAILURE_SENDING_EMAIL("ISE-101", "FAILURE SENDING EMAIL", "The email has not been sent due to some error."),


    DEVICE_NOT_FOUND("XXXX", "DEVICE NOT FOUND", "Couldn't find device for id %s."),
    NODE_NOT_FOUND("XXXY", "NODE NOT FOUND", "Couldn't find node.");

    private final String code;
    private final String title;
    private final String detail;

    _ExceptionConstants(final String code, final String title, final String detail) {
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
