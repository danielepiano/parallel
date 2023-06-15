package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class EventNotFoundException extends ResourceNotFoundException {
    public EventNotFoundException(final Integer id, final Integer headquartersId) {
        super(new Error(
                        _ExceptionConstants.EVENT_NOT_FOUND.getCode(),
                        _ExceptionConstants.EVENT_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.EVENT_NOT_FOUND.getDetail(), id, headquartersId)
                )
        );
    }

    public EventNotFoundException(final Integer id, final Integer headquartersId, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.EVENT_NOT_FOUND.getCode(),
                        _ExceptionConstants.EVENT_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.EVENT_NOT_FOUND.getDetail(), id, headquartersId)
                ), cause
        );
    }
}
