package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

public class HeadquartersNotDeletableException extends ConflictingResourceException {
    public HeadquartersNotDeletableException(final Integer id) {
        super(new Error(
                        _ExceptionConstants.HEADQUARTERS_NOT_DELETABLE.getCode(),
                        _ExceptionConstants.HEADQUARTERS_NOT_DELETABLE.getTitle(),
                        String.format(_ExceptionConstants.HEADQUARTERS_NOT_DELETABLE.getDetail(), id)
                )
        );
    }

    public HeadquartersNotDeletableException(final Integer id, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.HEADQUARTERS_NOT_DELETABLE.getCode(),
                        _ExceptionConstants.HEADQUARTERS_NOT_DELETABLE.getTitle(),
                        String.format(_ExceptionConstants.HEADQUARTERS_NOT_DELETABLE.getDetail(), id)
                ), cause
        );
    }
}
