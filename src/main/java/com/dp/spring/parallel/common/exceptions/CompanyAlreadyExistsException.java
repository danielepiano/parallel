package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

public class CompanyAlreadyExistsException extends ConflictingResourceException {
    public CompanyAlreadyExistsException(final String name, final String city, final String address) {
        super(new Error(
                        _ExceptionConstants.COMPANY_ALREADY_EXISTS.getCode(),
                        _ExceptionConstants.COMPANY_ALREADY_EXISTS.getTitle(),
                        String.format(_ExceptionConstants.COMPANY_ALREADY_EXISTS.getDetail(), name, city, address)
                )
        );
    }

    public CompanyAlreadyExistsException(final String name, final String city, final String address, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.COMPANY_ALREADY_EXISTS.getCode(),
                        _ExceptionConstants.COMPANY_ALREADY_EXISTS.getTitle(),
                        String.format(_ExceptionConstants.COMPANY_ALREADY_EXISTS.getDetail(), name, city, address)
                ), cause
        );
    }
}
