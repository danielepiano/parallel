package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.FailedAuthenticationException;
import com.dp.spring.springcore.model.error.Error;

public class TokenNotValidException extends FailedAuthenticationException {
    public TokenNotValidException() {
        super(new Error(
                        ExceptionConstants.TOKEN_NOT_VALID.getCode(),
                        ExceptionConstants.TOKEN_NOT_VALID.getTitle(),
                        ExceptionConstants.TOKEN_NOT_VALID.getDetail()
                )
        );
    }

    public TokenNotValidException(final Throwable cause) {
        super(new Error(
                        ExceptionConstants.TOKEN_NOT_VALID.getCode(),
                        ExceptionConstants.TOKEN_NOT_VALID.getTitle(),
                        ExceptionConstants.TOKEN_NOT_VALID.getDetail()
                ), cause
        );
    }
}
