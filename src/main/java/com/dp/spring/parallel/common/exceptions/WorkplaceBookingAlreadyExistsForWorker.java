package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

import java.time.LocalDate;

public class WorkplaceBookingAlreadyExistsForWorker extends ConflictingResourceException {
    public WorkplaceBookingAlreadyExistsForWorker(final Integer workerId, final LocalDate bookingDate) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_BOOKING_ALREADY_EXISTS_FOR_WORKER.getCode(),
                        _ExceptionConstants.WORKPLACE_BOOKING_ALREADY_EXISTS_FOR_WORKER.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_BOOKING_ALREADY_EXISTS_FOR_WORKER.getDetail(), workerId, bookingDate)
                )
        );
    }

    public WorkplaceBookingAlreadyExistsForWorker(final Integer workerId, final LocalDate bookingDate, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_BOOKING_ALREADY_EXISTS_FOR_WORKER.getCode(),
                        _ExceptionConstants.WORKPLACE_BOOKING_ALREADY_EXISTS_FOR_WORKER.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_BOOKING_ALREADY_EXISTS_FOR_WORKER.getDetail(), workerId, bookingDate)
                ), cause
        );
    }
}
