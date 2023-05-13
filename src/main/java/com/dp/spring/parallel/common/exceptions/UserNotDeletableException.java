package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

public class UserNotDeletableException extends ConflictingResourceException {
    public static final String AT_LEAST_ONE_ADMIN_CONFLICT = ": at least one admin should be active";

    public UserNotDeletableException(final String conflict) {
        super(new Error(
                        _ExceptionConstants.USER_NOT_DELETABLE.getCode(),
                        _ExceptionConstants.USER_NOT_DELETABLE.getTitle(),
                        String.format(_ExceptionConstants.USER_NOT_DELETABLE.getDetail(), conflict)
                )
        );
    }

    public UserNotDeletableException(final String conflict, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.USER_NOT_DELETABLE.getCode(),
                        _ExceptionConstants.USER_NOT_DELETABLE.getTitle(),
                        String.format(_ExceptionConstants.USER_NOT_DELETABLE.getDetail(), conflict)
                ), cause
        );
    }
}
