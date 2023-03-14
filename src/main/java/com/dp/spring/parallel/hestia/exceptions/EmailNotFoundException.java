package com.dp.spring.parallel.hestia.exceptions;

import com.dp.spring.parallel.common.exceptions.ExceptionConstants;
import com.dp.spring.springcore.v2.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.v2.model.error.Error;

public class EmailNotFoundException extends ResourceNotFoundException {
    public EmailNotFoundException(final String email) {
        super(new Error(
                        ExceptionConstants.EMAIL_NOT_FOUND.getCode(),
                        ExceptionConstants.EMAIL_NOT_FOUND.getTitle(),
                        String.format( ExceptionConstants.EMAIL_NOT_FOUND.getDetail(), email )
                )
        );
    }

    public EmailNotFoundException(String email, Throwable cause) {
        super(new Error(
                        ExceptionConstants.EMAIL_NOT_FOUND.getCode(),
                        ExceptionConstants.EMAIL_NOT_FOUND.getTitle(),
                        String.format( ExceptionConstants.EMAIL_NOT_FOUND.getDetail(), email )
                ), cause
        );
    }
}
