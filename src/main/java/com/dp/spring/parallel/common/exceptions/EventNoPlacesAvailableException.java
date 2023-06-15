package com.dp.spring.parallel.common.exceptions;

import com.dp.spring.springcore.exceptions.BadRequestException;
import com.dp.spring.springcore.model.error.Error;

public class EventNoPlacesAvailableException extends BadRequestException {
    public EventNoPlacesAvailableException(final Integer maxPlaces, final Integer eventId) {
        super(new Error(
                        _ExceptionConstants.EVENT_NO_PLACES_AVAILABLE.getCode(),
                        _ExceptionConstants.EVENT_NO_PLACES_AVAILABLE.getTitle(),
                        String.format(_ExceptionConstants.EVENT_NO_PLACES_AVAILABLE.getDetail(), maxPlaces, eventId)
                )
        );
    }

    public EventNoPlacesAvailableException(final Integer maxPlaces, final Integer eventId, final Throwable cause) {
        super(new Error(
                        _ExceptionConstants.EVENT_NO_PLACES_AVAILABLE.getCode(),
                        _ExceptionConstants.EVENT_NO_PLACES_AVAILABLE.getTitle(),
                        String.format(_ExceptionConstants.EVENT_NO_PLACES_AVAILABLE.getDetail(), maxPlaces, eventId)
                ), cause
        );
    }
}
