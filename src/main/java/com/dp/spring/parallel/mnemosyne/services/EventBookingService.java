package com.dp.spring.parallel.mnemosyne.services;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.mnemosyne.database.entities.EventBooking;

import java.time.LocalDate;
import java.util.List;

public interface EventBookingService {

    /**
     * Retrieving the logged user bookings from a given date.
     *
     * @param fromDate the date from which get the user bookings
     * @return the user event bookings from the given date
     */
    List<EventBooking> eventBookingsFromDate(LocalDate fromDate);

    /**
     * Booking for an event.
     *
     * @param headquartersId the id of the headquarters the event takes place
     * @param eventId        the id of the event to book for
     * @return the created booking
     */
    EventBooking book(
            Integer headquartersId,
            Integer eventId
    );

    /** todo adeguare per calcolo disponibili ?? rimuovere ??
     * Retrieving the workers already booked on given date and headquarters.
     *
     * @param date         the date
     * @param headquarters the headquarters
     * @return the workers already booked on the given date and headquarters
     **/
    //Set<User> workersOn(LocalDate date, Headquarters headquarters);

    /**
     * Cancelling a booking.
     *
     * @param headquartersId the id of the headquarters the event takes place
     * @param eventId        the id of the event booked for
     * @param bookingId      the id of the booking to cancel
     */
    void cancel(
            Integer headquartersId,
            Integer eventId,
            Integer bookingId
    );


    /**
     * Cancelling all bookings for a given headquarters.
     *
     * @param event the event to cancel the bookings of
     */
    void cancelAll(Event event);

}
