package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.BadRequestException;
import com.dp.spring.springcore.model.error.Error;

import java.time.LocalDate;

public class WorkplaceBookingParticipationNotSettableException extends BadRequestException {
    public WorkplaceBookingParticipationNotSettableException(final LocalDate bookingDate) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_BOOKING_PARTICIPATION_NOT_SETTABLE.getCode(),
                        _ExceptionConstants.WORKPLACE_BOOKING_PARTICIPATION_NOT_SETTABLE.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_BOOKING_PARTICIPATION_NOT_SETTABLE.getDetail(), bookingDate)
                )
        );
    }

    public WorkplaceBookingParticipationNotSettableException(final LocalDate bookingDate, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_BOOKING_PARTICIPATION_NOT_SETTABLE.getCode(),
                        _ExceptionConstants.WORKPLACE_BOOKING_PARTICIPATION_NOT_SETTABLE.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_BOOKING_PARTICIPATION_NOT_SETTABLE.getDetail(), bookingDate)
                ), cause
        );
    }
}
