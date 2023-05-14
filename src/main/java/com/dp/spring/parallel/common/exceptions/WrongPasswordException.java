package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.BadRequestException;
import com.dp.spring.springcore.model.error.Error;

public class WrongPasswordException extends BadRequestException {
    public WrongPasswordException() {
        super(new Error(
                        _ExceptionConstants.WRONG_PASSWORD.getCode(),
                        _ExceptionConstants.WRONG_PASSWORD.getTitle(),
                        _ExceptionConstants.WRONG_PASSWORD.getDetail()
                )
        );
    }

    public WrongPasswordException(final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WRONG_PASSWORD.getCode(),
                        _ExceptionConstants.WRONG_PASSWORD.getTitle(),
                        _ExceptionConstants.WRONG_PASSWORD.getDetail()
                ), cause
        );
    }
}
