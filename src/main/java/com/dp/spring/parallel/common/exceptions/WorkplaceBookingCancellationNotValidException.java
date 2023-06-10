package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.BadRequestException;
import com.dp.spring.springcore.model.error.Error;

import java.time.LocalDate;

public class WorkplaceBookingCancellationNotValidException extends BadRequestException {
    public WorkplaceBookingCancellationNotValidException(final LocalDate bookingDate) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_BOOKING_CANCELLATION_NOT_VALID.getCode(),
                        _ExceptionConstants.WORKPLACE_BOOKING_CANCELLATION_NOT_VALID.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_BOOKING_CANCELLATION_NOT_VALID.getDetail(), bookingDate)
                )
        );
    }

    public WorkplaceBookingCancellationNotValidException(final LocalDate bookingDate, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_BOOKING_CANCELLATION_NOT_VALID.getCode(),
                        _ExceptionConstants.WORKPLACE_BOOKING_CANCELLATION_NOT_VALID.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_BOOKING_CANCELLATION_NOT_VALID.getDetail(), bookingDate)
                ), cause
        );
    }
}
