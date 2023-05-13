package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class HeadquartersNotFoundException extends ResourceNotFoundException {
    public HeadquartersNotFoundException(final Integer id) {
        super(new Error(
                        _ExceptionConstants.HEADQUARTERS_NOT_FOUND.getCode(),
                        _ExceptionConstants.HEADQUARTERS_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.HEADQUARTERS_NOT_FOUND.getDetail(), id)
                )
        );
    }

    public HeadquartersNotFoundException(final Integer id, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.HEADQUARTERS_NOT_FOUND.getCode(),
                        _ExceptionConstants.HEADQUARTERS_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.HEADQUARTERS_NOT_FOUND.getDetail(), id)
                ), cause
        );
    }
}
