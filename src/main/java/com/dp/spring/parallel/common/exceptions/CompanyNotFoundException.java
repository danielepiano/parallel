package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class CompanyNotFoundException extends ResourceNotFoundException {
    public CompanyNotFoundException(final Integer id) {
        super(new Error(
                        ExceptionConstants.COMPANY_NOT_FOUND.getCode(),
                        ExceptionConstants.COMPANY_NOT_FOUND.getTitle(),
                        String.format(ExceptionConstants.COMPANY_NOT_FOUND.getDetail(), id)
                )
        );
    }

    public CompanyNotFoundException(final Integer id, final Throwable cause) {
        super(new Error(
                        ExceptionConstants.COMPANY_NOT_FOUND.getCode(),
                        ExceptionConstants.COMPANY_NOT_FOUND.getTitle(),
                        String.format(ExceptionConstants.COMPANY_NOT_FOUND.getDetail(), id)
                ), cause
        );
    }
}
