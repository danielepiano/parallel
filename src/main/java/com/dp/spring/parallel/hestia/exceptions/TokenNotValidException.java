package com.dp.spring.parallel.hestia.exceptions;

import com.dp.spring.parallel.common.exceptions.ExceptionConstants;
import com.dp.spring.springcore.v2.exceptions.FailedAuthenticationException;
import com.dp.spring.springcore.v2.model.error.Error;

public class TokenNotValidException extends FailedAuthenticationException {
    public TokenNotValidException() {
        super(new Error(
                        ExceptionConstants.TOKEN_NOT_VALID.getCode(),
                        ExceptionConstants.TOKEN_NOT_VALID.getTitle(),
                        ExceptionConstants.TOKEN_NOT_VALID.getDetail()
                )
        );
    }

    public TokenNotValidException(Throwable cause) {
        super(new Error(
                        ExceptionConstants.TOKEN_NOT_VALID.getCode(),
                        ExceptionConstants.TOKEN_NOT_VALID.getTitle(),
                        ExceptionConstants.TOKEN_NOT_VALID.getDetail()
                ), cause
        );
    }
}
