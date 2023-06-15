package com.dp.spring.parallel.mnemosyne.api.controllers.impl;

import com.dp.spring.parallel.mnemosyne.api.controllers.EventBookingController;
import com.dp.spring.parallel.mnemosyne.api.dtos.EventBookingResponseDTO;
import com.dp.spring.parallel.mnemosyne.api.dtos.UserEventBookingResponseDTO;
import com.dp.spring.parallel.mnemosyne.database.entities.EventBooking;
import com.dp.spring.parallel.mnemosyne.services.EventBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static java.util.Optional.ofNullable;

@RestController
@RequiredArgsConstructor
public class EventBookingControllerImpl implements EventBookingController {
    private final EventBookingService eventBookingService;


    @Override
    public List<UserEventBookingResponseDTO> userEventBookingsFromDate(LocalDate fromDate) {
        return this.eventBookingService.eventBookingsFromDate(ofNullable(fromDate).orElse(LocalDate.now()))
                .stream()
                .map(booking -> {
                    // todo calc available places
                    return UserEventBookingResponseDTO.of(null, booking);
                })
                .toList();
    }

    @Override
    public EventBookingResponseDTO bookEvent(Integer headquartersId, Integer eventId) {
        final EventBooking booking = this.eventBookingService.book(headquartersId, eventId);
        return EventBookingResponseDTO.of(booking);
    }

    @Override
    public void cancelBooking(Integer headquartersId, Integer eventId, Integer bookingId) {
        this.eventBookingService.cancel(headquartersId, eventId, bookingId);
    }

}
