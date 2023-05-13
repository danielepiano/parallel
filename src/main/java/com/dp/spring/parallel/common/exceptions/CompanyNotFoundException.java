package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class CompanyNotFoundException extends ResourceNotFoundException {
    public CompanyNotFoundException(final Integer id) {
        super(new Error(
                        _ExceptionConstants.COMPANY_NOT_FOUND.getCode(),
                        _ExceptionConstants.COMPANY_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.COMPANY_NOT_FOUND.getDetail(), id)
                )
        );
    }

    public CompanyNotFoundException(final Integer id, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.COMPANY_NOT_FOUND.getCode(),
                        _ExceptionConstants.COMPANY_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.COMPANY_NOT_FOUND.getDetail(), id)
                ), cause
        );
    }
}
