package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.BadRequestException;
import com.dp.spring.springcore.model.error.Error;

public class EventNotBookableException extends BadRequestException {
    public EventNotBookableException(final Integer eventId) {
        super(new Error(
                        _ExceptionConstants.EVENT_NOT_BOOKABLE.getCode(),
                        _ExceptionConstants.EVENT_NOT_BOOKABLE.getTitle(),
                        String.format(_ExceptionConstants.EVENT_NOT_BOOKABLE.getDetail(), eventId)
                )
        );
    }

    public EventNotBookableException(final Integer eventId, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.EVENT_NOT_BOOKABLE.getCode(),
                        _ExceptionConstants.EVENT_NOT_BOOKABLE.getTitle(),
                        String.format(_ExceptionConstants.EVENT_NOT_BOOKABLE.getDetail(), eventId)
                ), cause
        );
    }
}
