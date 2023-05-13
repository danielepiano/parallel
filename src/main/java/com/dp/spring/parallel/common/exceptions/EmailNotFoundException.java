package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class EmailNotFoundException extends ResourceNotFoundException {
    public EmailNotFoundException(final String email) {
        super(new Error(
                        _ExceptionConstants.EMAIL_NOT_FOUND.getCode(),
                        _ExceptionConstants.EMAIL_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.EMAIL_NOT_FOUND.getDetail(), email)
                )
        );
    }

    public EmailNotFoundException(final String email, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.EMAIL_NOT_FOUND.getCode(),
                        _ExceptionConstants.EMAIL_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.EMAIL_NOT_FOUND.getDetail(), email)
                ), cause
        );
    }
}
