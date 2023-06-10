package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ConflictingResourceException;
import com.dp.spring.springcore.model.error.Error;

import java.time.LocalDate;

public class WorkplaceNotAvailableForBooking extends ConflictingResourceException {
    public WorkplaceNotAvailableForBooking(final Integer workplaceId, final LocalDate bookingDate) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_NOT_AVAILABLE_FOR_BOOKING.getCode(),
                        _ExceptionConstants.WORKPLACE_NOT_AVAILABLE_FOR_BOOKING.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_NOT_AVAILABLE_FOR_BOOKING.getDetail(), workplaceId, bookingDate)
                )
        );
    }

    public WorkplaceNotAvailableForBooking(final Integer workplaceId, final LocalDate bookingDate, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_NOT_AVAILABLE_FOR_BOOKING.getCode(),
                        _ExceptionConstants.WORKPLACE_NOT_AVAILABLE_FOR_BOOKING.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_NOT_AVAILABLE_FOR_BOOKING.getDetail(), workplaceId, bookingDate)
                ), cause
        );
    }
}
