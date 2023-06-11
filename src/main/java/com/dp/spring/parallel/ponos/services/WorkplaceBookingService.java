package com.dp.spring.parallel.ponos.services;

import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.ponos.api.dtos.WorkplaceBookingRequestDTO;
import com.dp.spring.parallel.ponos.database.entities.WorkplaceBooking;

import java.time.LocalDate;
import java.util.List;

public interface WorkplaceBookingService {

    /**
     * Retrieving the principal bookings from a given date.
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
     * Setting the participation of the principal for the given booking.
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
