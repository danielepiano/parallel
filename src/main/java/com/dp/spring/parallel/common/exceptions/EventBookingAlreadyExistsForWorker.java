package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

public class EventBookingAlreadyExistsForWorker extends ConflictingResourceException {
    public EventBookingAlreadyExistsForWorker(final Integer workerId, final Integer eventId) {
        super(new Error(
                        _ExceptionConstants.EVENT_BOOKING_ALREADY_EXISTS_FOR_WORKER.getCode(),
                        _ExceptionConstants.EVENT_BOOKING_ALREADY_EXISTS_FOR_WORKER.getTitle(),
                        String.format(_ExceptionConstants.EVENT_BOOKING_ALREADY_EXISTS_FOR_WORKER.getDetail(), workerId, eventId)
                )
        );
    }

    public EventBookingAlreadyExistsForWorker(final Integer workerId, final Integer eventId, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.EVENT_BOOKING_ALREADY_EXISTS_FOR_WORKER.getCode(),
                        _ExceptionConstants.EVENT_BOOKING_ALREADY_EXISTS_FOR_WORKER.getTitle(),
                        String.format(_ExceptionConstants.EVENT_BOOKING_ALREADY_EXISTS_FOR_WORKER.getDetail(), workerId, eventId)
                ), cause
        );
    }
}
