package com.dp.spring.parallel.mnemosyne.api.controllers;

import com.dp.spring.parallel.mnemosyne.api.dtos.EventBookingResponseDTO;
import com.dp.spring.parallel.mnemosyne.api.dtos.UserEventBookingResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.ROLE_COMPANY_MANAGER_VALUE;
import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.ROLE_EMPLOYEE_VALUE;

@RequestMapping("/api/v1")
public interface EventBookingController {

    @GetMapping(path = "/events/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_COMPANY_MANAGER_VALUE, ROLE_EMPLOYEE_VALUE})
    List<UserEventBookingResponseDTO> userEventBookingsFromDate(
            @RequestParam(name = "fromDate", required = false) LocalDate fromDate
    );

    @PostMapping(
            path = "/headquarters/{headquartersId}/events/{eventId}/bookings",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured({ROLE_COMPANY_MANAGER_VALUE, ROLE_EMPLOYEE_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    EventBookingResponseDTO bookEvent(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("eventId") Integer eventId
    );

    @DeleteMapping(
            path = "/headquarters/{headquartersId}/events/{eventId}/bookings/{bookingId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured({ROLE_COMPANY_MANAGER_VALUE, ROLE_EMPLOYEE_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void cancelBooking(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("eventId") Integer eventId,
            @PathVariable("bookingId") Integer bookingId
    );

}
