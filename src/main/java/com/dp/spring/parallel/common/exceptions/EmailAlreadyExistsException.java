package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

public class EmailAlreadyExistsException extends ConflictingResourceException {
    public EmailAlreadyExistsException(final String email) {
        super(new Error(
                        _ExceptionConstants.EMAIL_ALREADY_EXISTS.getCode(),
                        _ExceptionConstants.EMAIL_ALREADY_EXISTS.getTitle(),
                        String.format(_ExceptionConstants.EMAIL_ALREADY_EXISTS.getDetail(), email)
                )
        );
    }

    public EmailAlreadyExistsException(final String email, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.EMAIL_ALREADY_EXISTS.getCode(),
                        _ExceptionConstants.EMAIL_ALREADY_EXISTS.getTitle(),
                        String.format(_ExceptionConstants.EMAIL_ALREADY_EXISTS.getDetail(), email)
                ), cause
        );
    }
}
