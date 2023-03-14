package com.dp.spring.parallel.hestia.exceptions;

import com.dp.spring.parallel.common.exceptions.ExceptionConstants;
import com.dp.spring.springcore.v2.exceptions.FailedAuthenticationException;
import com.dp.spring.springcore.v2.model.error.Error;

public class WrongCredentialsException extends FailedAuthenticationException {
    public WrongCredentialsException() {
        super(new Error(
                        ExceptionConstants.WRONG_CREDENTIALS.getCode(),
                        ExceptionConstants.WRONG_CREDENTIALS.getTitle(),
                        ExceptionConstants.WRONG_CREDENTIALS.getDetail()
                )
        );
    }

    public WrongCredentialsException(Throwable cause) {
        super(new Error(
                        ExceptionConstants.WRONG_CREDENTIALS.getCode(),
                        ExceptionConstants.WRONG_CREDENTIALS.getTitle(),
                        ExceptionConstants.WRONG_CREDENTIALS.getDetail()
                ), cause
        );
    }
}
