package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import com.dp.spring.springcore.model.error.Error;

public class WorkplaceBookingNotFoundException extends ResourceNotFoundException {
    public WorkplaceBookingNotFoundException(final Integer id, final Integer workplaceId) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getCode(),
                        _ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getDetail(), id, workplaceId)
                )
        );
    }

    public WorkplaceBookingNotFoundException(final Integer id, final Integer workplaceId, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getCode(),
                        _ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getTitle(),
                        String.format(_ExceptionConstants.WORKPLACE_BOOKING_NOT_FOUND.getDetail(), id, workplaceId)
                ), cause
        );
    }
}
