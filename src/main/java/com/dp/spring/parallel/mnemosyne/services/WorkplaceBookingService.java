package com.dp.spring.parallel.mnemosyne.services;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.mnemosyne.api.dtos.WorkplaceBookingRequestDTO;
import com.dp.spring.parallel.mnemosyne.database.entities.WorkplaceBooking;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface WorkplaceBookingService {

    /**
     * Retrieving the logged user bookings from a given date.
     *
     * @param fromDate the date from which get the user bookings
     * @return the user workplaces from the given date
     */
    List<WorkplaceBooking> workplaceBookingsFromDate(LocalDate fromDate);

    /**
     * Booking a workplace.
     *
     * @param workspaceId the id of the workspace to book
     * @param workplaceId the id of the workplace to book
     * @param bookRequest the booking request
     * @return the created booking
     */
    WorkplaceBooking book(
            Integer workspaceId,
            Integer workplaceId,
            WorkplaceBookingRequestDTO bookRequest
    );

    /**
     * Retrieving the workers already booked on given date and headquarters.
     *
     * @param date         the date
     * @param headquarters the headquarters
     * @return the workers already booked on the given date and headquarters
     */
    Set<User> workersOn(LocalDate date, Headquarters headquarters);

    /**
     * Setting the participation of the logged user for the given booking.
     *
     * @param workspaceId the id of the workspace booked
     * @param workplaceId the id of the workplace booked
     * @param bookingId   the id of the booking to update
     * @return the updated booking
     */
    WorkplaceBooking setParticipation(
            Integer workspaceId,
            Integer workplaceId,
            Integer bookingId
    );

    /**
     * Cancelling a booking.
     *
     * @param workspaceId the id of the workspace booked
     * @param workplaceId the id of the workplace booked
     * @param bookingId   the id of the booking to cancel
     */
    void cancel(
            Integer workspaceId,
            Integer workplaceId,
            Integer bookingId
    );


    /**
     * Cancelling all bookings for a given workplace.
     *
     * @param workplace the workplace to cancel the booking of
     */
    void cancelAll(Workplace workplace);

}
