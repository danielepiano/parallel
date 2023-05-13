package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

public class CompanyNotDeletableException extends ConflictingResourceException {
    public CompanyNotDeletableException(final Integer id) {
        super(new Error(
                        _ExceptionConstants.COMPANY_NOT_DELETABLE.getCode(),
                        _ExceptionConstants.COMPANY_NOT_DELETABLE.getTitle(),
                        String.format(_ExceptionConstants.COMPANY_NOT_DELETABLE.getDetail(), id)
                )
        );
    }

    public CompanyNotDeletableException(final Integer id, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.COMPANY_NOT_DELETABLE.getCode(),
                        _ExceptionConstants.COMPANY_NOT_DELETABLE.getTitle(),
                        String.format(_ExceptionConstants.COMPANY_NOT_DELETABLE.getDetail(), id)
                ), cause
        );
    }
}
