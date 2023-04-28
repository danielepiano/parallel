package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class EmailNotFoundException extends ResourceNotFoundException {
    public EmailNotFoundException(final String email) {
        super(new Error(
                        ExceptionConstants.EMAIL_NOT_FOUND.getCode(),
                        ExceptionConstants.EMAIL_NOT_FOUND.getTitle(),
                        String.format(ExceptionConstants.EMAIL_NOT_FOUND.getDetail(), email)
                )
        );
    }

    public EmailNotFoundException(final String email, final Throwable cause) {
        super(new Error(
                        ExceptionConstants.EMAIL_NOT_FOUND.getCode(),
                        ExceptionConstants.EMAIL_NOT_FOUND.getTitle(),
                        String.format(ExceptionConstants.EMAIL_NOT_FOUND.getDetail(), email)
                ), cause
        );
    }
}
