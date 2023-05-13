package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

public class HeadquartersAlreadyExistsException extends ConflictingResourceException {
    public HeadquartersAlreadyExistsException(final String city, final String address) {
        super(new Error(
                        _ExceptionConstants.HEADQUARTERS_ALREADY_EXISTS.getCode(),
                        _ExceptionConstants.HEADQUARTERS_ALREADY_EXISTS.getTitle(),
                        String.format(_ExceptionConstants.HEADQUARTERS_ALREADY_EXISTS.getDetail(), city, address)
                )
        );
    }

    public HeadquartersAlreadyExistsException(final String city, final String address, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.HEADQUARTERS_ALREADY_EXISTS.getCode(),
                        _ExceptionConstants.HEADQUARTERS_ALREADY_EXISTS.getTitle(),
                        String.format(_ExceptionConstants.HEADQUARTERS_ALREADY_EXISTS.getDetail(), city, address)
                ), cause
        );
    }
}
