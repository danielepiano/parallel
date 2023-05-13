package com.dp.spring.parallel.hermes.exceptions;

import com.dp.spring.parallel.common.exceptions._ExceptionConstants;
import com.dp.spring.springcore.exceptions.InternalServerErrorException;
import com.dp.spring.springcore.model.error.Error;

/**
 * Some error occurred when trying sending an email.
 */
public class FailureSendingEmail extends InternalServerErrorException {
    public FailureSendingEmail() {
        super(new Error(
                        _ExceptionConstants.FAILURE_SENDING_EMAIL.getCode(),
                        _ExceptionConstants.FAILURE_SENDING_EMAIL.getTitle(),
                        _ExceptionConstants.FAILURE_SENDING_EMAIL.getDetail()
                )
        );
    }

    public FailureSendingEmail(Throwable cause) {
        super(new Error(
                        _ExceptionConstants.FAILURE_SENDING_EMAIL.getCode(),
                        _ExceptionConstants.FAILURE_SENDING_EMAIL.getTitle(),
                        _ExceptionConstants.FAILURE_SENDING_EMAIL.getDetail()
                ), cause
        );
    }
}
