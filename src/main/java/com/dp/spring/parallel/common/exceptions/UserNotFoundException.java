package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(final String role, final Integer id) {
        super(new Error(
                        _ExceptionConstants.USER_NOT_FOUND.getCode(),
                        _ExceptionConstants.USER_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.USER_NOT_FOUND.getDetail(), role, id)
                )
        );
    }

    public UserNotFoundException(final String role, final Integer id, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.USER_NOT_FOUND.getCode(),
                        _ExceptionConstants.USER_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.USER_NOT_FOUND.getDetail(), role, id)
                ), cause
        );
    }
}
