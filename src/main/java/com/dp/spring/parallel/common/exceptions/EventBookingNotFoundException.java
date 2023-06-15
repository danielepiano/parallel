package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class EventBookingNotFoundException extends ResourceNotFoundException {
    public EventBookingNotFoundException(final Integer id, final Integer eventId) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getCode(),
                        _ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getDetail(), id, eventId)
                )
        );
    }

    public EventBookingNotFoundException(final Integer id, final Integer eventId, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getCode(),
                        _ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getDetail(), id, eventId)
                ), cause
        );
    }
}
