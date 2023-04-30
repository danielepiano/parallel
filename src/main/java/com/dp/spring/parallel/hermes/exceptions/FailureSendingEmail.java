package com.dp.spring.parallel.hermes.exceptions;

import com.dp.spring.parallel.common.exceptions.ExceptionConstants;
import com.dp.spring.springcore.exceptions.InternalServerErrorException;
import com.dp.spring.springcore.model.error.Error;

/**
 * Some error occurred when trying sending an email.
 */
public class FailureSendingEmail extends InternalServerErrorException {
    public FailureSendingEmail() {
        super(new Error(
                        ExceptionConstants.FAILURE_SENDING_EMAIL.getCode(),
                        ExceptionConstants.FAILURE_SENDING_EMAIL.getTitle(),
                        ExceptionConstants.FAILURE_SENDING_EMAIL.getDetail()
                )
        );
    }

    public FailureSendingEmail(Throwable cause) {
        super(new Error(
                        ExceptionConstants.FAILURE_SENDING_EMAIL.getCode(),
                        ExceptionConstants.FAILURE_SENDING_EMAIL.getTitle(),
                        ExceptionConstants.FAILURE_SENDING_EMAIL.getDetail()
                ), cause
        );
    }
}
