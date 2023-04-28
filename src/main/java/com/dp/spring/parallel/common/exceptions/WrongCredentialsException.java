package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.FailedAuthenticationException;
import com.dp.spring.springcore.model.error.Error;

public class WrongCredentialsException extends FailedAuthenticationException {
    public WrongCredentialsException() {
        super(new Error(
                        ExceptionConstants.WRONG_CREDENTIALS.getCode(),
                        ExceptionConstants.WRONG_CREDENTIALS.getTitle(),
                        ExceptionConstants.WRONG_CREDENTIALS.getDetail()
                )
        );
    }

    public WrongCredentialsException(final Throwable cause) {
        super(new Error(
                        ExceptionConstants.WRONG_CREDENTIALS.getCode(),
                        ExceptionConstants.WRONG_CREDENTIALS.getTitle(),
                        ExceptionConstants.WRONG_CREDENTIALS.getDetail()
                ), cause
        );
    }
}
