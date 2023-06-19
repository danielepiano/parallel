package com.dp.spring.parallel.mnemosyne.services.impl;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.agora.services.EventService;
import com.dp.spring.parallel.common.exceptions.*;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hermes.utils.EmailMessageParser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.mnemosyne.database.entities.EventBooking;
import com.dp.spring.parallel.mnemosyne.database.repositories.EventBookingRepository;
import com.dp.spring.parallel.mnemosyne.services.EventBookingService;
import com.dp.spring.springcore.exceptions.BaseExceptionConstants;
import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Workspace operations service implementation.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventBookingServiceImpl extends BusinessService implements EventBookingService {
    private final EmailNotificationService emailNotificationService;
    private final HeadquartersService headquartersService;
    private final EventService eventService;

    private final EventBookingRepository eventBookingRepository;


    private static final String EVENT_BOOKING_NOTIFICATION_TITLE = "Prenotazione effettuata con successo!";
    private static final String EVENT_BOOKING_NOTIFICATION_MESSAGE_PATH = "email/event-booking-template.html";


    /**
     * {@inheritDoc}
     *
     * @param fromDate the date from which get the user bookings
     * @return the user event bookings from the given date
     */
    @Override
    public List<EventBooking> eventBookingsFromDate(LocalDate fromDate) {
        final User worker = super.getPrincipalOrThrow();
        return this.eventBookingRepository.findAllByWorkerAndEventOnDateGreaterThanEqualOrderByEventOnDate(worker, fromDate);
    }

    /**
     * {@inheritDoc} <br>
     * Booking made by the principal. After getting the event, verifying whether the principal has already booked for it.
     *
     * @param eventId the id of the event to book for
     * @return the created booking
     */
    @Override
    public EventBooking book(Integer headquartersId, Integer eventId) {
        final Event event = this.eventService.event(headquartersId, eventId);
        final User worker = super.getPrincipalOrThrow();

        this.checkBookingPerformableOrThrow(worker, event);

        EventBooking booking = new EventBooking()
                .setWorker(worker)
                .setEvent(event);
        booking = this.eventBookingRepository.save(booking);

        this.buildAndSendNotification(booking);

        return booking;
    }

    /**
     * {@inheritDoc}
     * Principal can cancel his and only his bookings. Booking is also cancellable only before the booking date. <br>
     * Since it's a DELETE Http method, not found exceptions are ignored and NO_CONTENT is returned.
     *
     * @param headquartersId the id of the headquarters the event takes place
     * @param eventId        the id of the event booked for
     * @param bookingId      the id of the booking to cancel
     */
    @Override
    public void cancel(Integer headquartersId, Integer eventId, Integer bookingId) {
        try {
            final Event event = this.eventService.event(headquartersId, eventId);
            final User worker = super.getPrincipalOrThrow();

            final EventBooking booking = this.eventBookingRepository.findByIdAndEvent(bookingId, event)
                    .orElseThrow(() -> new EventBookingNotFoundException(eventId, headquartersId));

            this.checkCancellationPerformableOrThrow(worker, booking);

            this.eventBookingRepository.softDelete(booking);
        } catch (ResourceNotFoundException ignored) {
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param event the event to cancel the bookings of
     */
    @Override
    public void cancelAll(Event event) {
        // Get all the bookings for the event, and soft delete each of them
        this.eventBookingRepository.findAllByEvent(event)
                .forEach(eventBookingRepository::softDelete);
    }

    /**
     * {@inheritDoc}
     *
     * @param event the event to count the bookings of
     * @return the number of the available places
     */
    @Override
    public long countAvailablePlaces(Event event) {
        return event.getMaxPlaces() - this.eventBookingRepository.countByEvent(event);
    }


    /**
     * Checking if event is past.
     * Checking that user has not already booked for the same event. <br>
     * Checking if event is sold out.
     *
     * @param worker the worker trying to book
     * @param event  the event to book for
     */
    private void checkBookingPerformableOrThrow(final User worker, final Event event) {
        if (LocalDate.now().isAfter(event.getOnDate())) {
            throw new EventNotBookableException(event.getId());
        }
        if (this.eventBookingRepository.existsByWorkerAndEvent(worker, event)) {
            throw new EventBookingAlreadyExistsForWorker(worker.getId(), event.getId());
        }
        if (this.eventBookingRepository.countByEvent(event) >= event.getMaxPlaces()) {
            throw new EventNoPlacesAvailableException(event.getMaxPlaces(), event.getId());
        }
    }

    /**
     * Checking if event booking cancellation is possible: worker should only be able to cancel his own bookings,
     * and can't cancel a past (or current date) booking.
     *
     * @param worker  the worker trying to cancel a booking
     * @param booking the booking to cancel
     */
    private void checkCancellationPerformableOrThrow(final User worker, final EventBooking booking) {
        if (!Objects.equals(worker.getId(), booking.getWorker().getId())) {
            throw new AccessDeniedException(BaseExceptionConstants.ACCESS_DENIED.getDetail());
        }
        if (!LocalDate.now().isBefore(booking.getEvent().getOnDate())) {
            throw new WorkplaceBookingCancellationNotValidException(booking.getEvent().getOnDate());
        }
    }


    /**
     * Building and sending notification message to confirm booking has been successful.
     *
     * @param booking the booking to notify for
     */
    private void buildAndSendNotification(final EventBooking booking) {
        // Building message to confirm password changing
        final String message = this.emailNotificationService.buildMessage(
                EVENT_BOOKING_NOTIFICATION_MESSAGE_PATH,
                Map.of(
                        EmailMessageParser.Placeholder.FIRST_NAME, booking.getWorker().getFirstName(),
                        EmailMessageParser.Placeholder.LAST_NAME, booking.getWorker().getLastName(),
                        EmailMessageParser.Placeholder.COMPANY_NAME, booking.getEvent().getHeadquarters().getCompany().getName(),
                        EmailMessageParser.Placeholder.HEADQUARTERS_CITY, booking.getEvent().getHeadquarters().getCity(),
                        EmailMessageParser.Placeholder.HEADQUARTERS_ADDRESS, booking.getEvent().getHeadquarters().getAddress(),
                        EmailMessageParser.Placeholder.EVENT_DATE, booking.getEvent().getOnDate().toString(),
                        EmailMessageParser.Placeholder.EVENT_START_TIME, booking.getEvent().getStartTime().toString(),
                        EmailMessageParser.Placeholder.EVENT_END_TIME, booking.getEvent().getEndTime().toString(),
                        EmailMessageParser.Placeholder.EVENT_NAME, booking.getEvent().getName(),
                        EmailMessageParser.Placeholder.EVENT_MAX_PLACES, booking.getEvent().getMaxPlaces().toString()
                )
        );
        // Sending email
        this.emailNotificationService.notify(
                booking.getWorker().getEmail(),
                EVENT_BOOKING_NOTIFICATION_TITLE,
                message
        );
    }

}
