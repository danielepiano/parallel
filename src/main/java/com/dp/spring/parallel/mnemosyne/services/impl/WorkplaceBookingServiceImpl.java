package com.dp.spring.parallel.mnemosyne.services.impl;

import com.dp.spring.parallel.common.exceptions.*;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkplaceRepository;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkspaceRepository;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hermes.utils.EmailMessageParser;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.mnemosyne.api.dtos.WorkplaceBookingRequestDTO;
import com.dp.spring.parallel.mnemosyne.database.entities.WorkplaceBooking;
import com.dp.spring.parallel.mnemosyne.database.repositories.WorkplaceBookingRepository;
import com.dp.spring.parallel.mnemosyne.services.WorkplaceBookingService;
import com.dp.spring.parallel.mnemosyne.services.observer.HeadquartersWorkplaceBookingsObserverService;
import com.dp.spring.parallel.mnemosyne.services.observer.HeadquartersWorkplaceBookingsObserverService.Context;
import com.dp.spring.springcore.exceptions.BaseExceptionConstants;
import com.dp.spring.springcore.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Workspace operations service implementation.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WorkplaceBookingServiceImpl extends BusinessService implements WorkplaceBookingService {
    private final EmailNotificationService emailNotificationService;
    private final HeadquartersService headquartersService;
    private final WorkplaceService workplaceService;

    private final WorkspaceRepository workspaceRepository;
    private final WorkplaceRepository workplaceRepository;
    private final WorkplaceBookingRepository workplaceBookingRepository;


    private final HeadquartersWorkplaceBookingsObserverService headquartersWorkplaceBookingsObserverService;


    private static final String WORKPLACE_BOOKING_NOTIFICATION_TITLE = "Prenotazione effettuata con successo!";
    private static final String WORKPLACE_BOOKING_NOTIFICATION_MESSAGE_PATH = "email/workplace-booking-template.html";


    /**
     * {@inheritDoc}
     *
     * @param fromDate the date from which get the user bookings
     * @return the user workplace bookings from the given date
     */
    @Override
    public List<WorkplaceBooking> workplaceBookingsFromDate(LocalDate fromDate) {
        final User worker = super.getPrincipalOrThrow();
        return this.workplaceBookingRepository.findAllByWorkerAndBookingDateGreaterThanEqual(worker, fromDate, Sort.by(Sort.Direction.ASC, "bookingDate"));
    }


    /**
     * {@inheritDoc} <br>
     * Booking made by the principal. After getting the workplace, verifying whether the principal has already booked
     * another workplace on the same date and whether the workplace is available on that date.<br>
     * Booking date in {@link WorkplaceBookingRequestDTO} validated after serialization by inner constraint validators.
     *
     * @param workspaceId the id of the workspace to book
     * @param workplaceId the id of the workplace to book
     * @param bookRequest the booking request
     * @return the created booking
     */
    @Override
    public WorkplaceBooking book(Integer workspaceId, Integer workplaceId, WorkplaceBookingRequestDTO bookRequest) {
        final Workplace workplace = this.getWorkplaceOrThrow(workspaceId, workplaceId);
        final User worker = super.getPrincipalOrThrow();

        this.checkBookingDateConflicts(bookRequest.getBookingDate(), worker, workplace);

        WorkplaceBooking booking = new WorkplaceBooking()
                .setWorker(worker)
                .setWorkplace(workplace)
                .setBookingDate(bookRequest.getBookingDate());
        booking = this.workplaceBookingRepository.save(booking);

        this.buildAndSendNotification(booking);

        this.notifyHeadquartersObservers(bookRequest.getBookingDate(), workplace.getWorkspace().getHeadquarters());

        return booking;
    }


    /**
     * {@inheritDoc}
     *
     * @param date         the date
     * @param headquarters the headquarters
     * @return the workers already booked on the given date and headquarters
     */
    @Override
    public Set<User> workersOn(LocalDate date, Headquarters headquarters) {
        return this.workplaceBookingRepository.findAllWorkersBookedByHeadquartersAndBookingDate(headquarters, date);
    }


    /**
     * {@inheritDoc} <br>
     * Operation performable only by headquarters receptionist: needed check whether the given workplace is located in
     * a headquarters the principal has access to. <br>
     * Presence can only be marked for a booking on the actual booking date.
     *
     * @param workspaceId the id of the workspace booked
     * @param workplaceId the id of the workplace booked
     * @param bookingId   the id of the booking to update
     * @return the updated booking
     */
    @Override
    public WorkplaceBooking setParticipation(Integer workspaceId, Integer workplaceId, Integer bookingId) {
        final Workplace workplace = this.getWorkplaceOrThrow(workspaceId, workplaceId);

        // Check receptionist permission to access workplace
        super.checkHeadquartersReceptionistPrincipalScopeOrThrow(
                workplace.getWorkspace().getHeadquarters().getCompany().getId(),
                (HeadquartersReceptionistUser) super.getPrincipalOrThrow()
        );

        final WorkplaceBooking booking = this.workplaceBookingRepository.findByIdAndWorkplace(workplaceId, workplace)
                .orElseThrow(() -> new WorkplaceBookingNotFoundException(bookingId, workplaceId));

        this.checkParticipationMarkingDateOrThrow(booking);

        booking.setPresent(true);
        return this.workplaceBookingRepository.save(booking);
    }


    /**
     * {@inheritDoc} <br>
     * Principal can cancel his and only his bookings. Booking is also cancellable only before the booking date. <br>
     * Since it's a DELETE Http method, not found exceptions are ignored and NO_CONTENT is returned.
     *
     * @param workspaceId the id of the workspace booked
     * @param workplaceId the id of the workplace booked
     * @param bookingId   the id of the booking to cancel
     */
    @Override
    public void cancel(Integer workspaceId, Integer workplaceId, Integer bookingId) {
        try {
            final Workplace workplace = this.getWorkplaceOrThrow(workspaceId, workplaceId);
            final User worker = super.getPrincipalOrThrow();

            final WorkplaceBooking booking = this.workplaceBookingRepository.findByIdAndWorkplace(bookingId, workplace)
                    .orElseThrow(() -> new WorkplaceBookingNotFoundException(bookingId, workplaceId));

            this.checkCancellationPerformableOrThrow(worker, booking);
            this.workplaceBookingRepository.softDelete(booking);
        } catch (ResourceNotFoundException ignored) {
        }
    }


    /**
     * {@inheritDoc}
     *
     * @param workplace the workplace to cancel the bookings of
     */
    public void cancelAll(Workplace workplace) {
        // Get all the bookings for the workplace, and soft delete each of them
        this.workplaceBookingRepository.findAllByWorkplace(workplace)
                .forEach(workplaceBookingRepository::softDelete);
    }


    /**
     * Getting the workplace resource given the ids of the workspace and the workplace, if it exists.
     *
     * @param workspaceId the id of the workspace
     * @param workplaceId the id of the workplace
     * @return the workplace
     */
    private Workplace getWorkplaceOrThrow(final Integer workspaceId, final Integer workplaceId) {
        final Workspace workspace = super.getResourceOrThrow(
                workspaceId, workspaceRepository, new WorkspaceNotFoundException(workspaceId, null)
        );
        return this.workplaceRepository.findByIdAndWorkspace(workplaceId, workspace)
                .orElseThrow(() -> new WorkplaceNotFoundException(workplaceId, workspaceId));
    }

    /**
     * Checking that workplace is available on the given date.<br>
     * Checking that user has not already booked another workplace on the same given date.
     *
     * @param bookingDate the booking date
     * @param worker      the worker trying to book
     * @param workplace   the workplace to book
     */
    private void checkBookingDateConflicts(final LocalDate bookingDate, final User worker, final Workplace workplace) {
        if (this.workplaceBookingRepository.countAllByWorkerAndBookingDate(worker, bookingDate) > 0) {
            throw new WorkplaceBookingAlreadyExistsForWorker(worker.getId(), bookingDate);
        }
        if (this.workplaceBookingRepository.countAllByWorkplaceAndBookingDate(workplace, bookingDate) > 0) {
            throw new WorkplaceNotAvailableForBooking(workplace.getId(), bookingDate);
        }
    }

    /**
     * Checking that current date is the booking date.
     *
     * @param booking the booking to set participation to
     */
    private void checkParticipationMarkingDateOrThrow(final WorkplaceBooking booking) {
        if (!LocalDate.now().equals(booking.getBookingDate())) {
            throw new WorkplaceBookingParticipationNotSettableException(booking.getBookingDate());
        }
    }

    /**
     * Checking if workplace booking cancellation is possible: worker should only be able to cancel his own bookings,
     * and can't cancel a past (or current date) booking.
     *
     * @param worker  the worker trying to cancel a booking
     * @param booking the booking to cancel
     */
    private void checkCancellationPerformableOrThrow(final User worker, final WorkplaceBooking booking) {
        if (!Objects.equals(worker.getId(), booking.getWorker().getId())) {
            throw new AccessDeniedException(BaseExceptionConstants.ACCESS_DENIED.getDetail());
        }
        if (!LocalDate.now().isBefore(booking.getBookingDate())) {
            throw new WorkplaceBookingCancellationNotValidException(booking.getBookingDate());
        }
    }

    /**
     * Building and sending notification message to confirm booking has been successful.
     *
     * @param booking the booking to notify for
     */
    private void buildAndSendNotification(final WorkplaceBooking booking) {
        // Building message to confirm password changing
        final String message = this.emailNotificationService.buildMessage(
                WORKPLACE_BOOKING_NOTIFICATION_MESSAGE_PATH,
                Map.of(
                        EmailMessageParser.Placeholder.FIRST_NAME, booking.getWorker().getFirstName(),
                        EmailMessageParser.Placeholder.LAST_NAME, booking.getWorker().getLastName(),
                        EmailMessageParser.Placeholder.COMPANY_NAME, booking.getWorkplace().getWorkspace().getHeadquarters().getCompany().getName(),
                        EmailMessageParser.Placeholder.HEADQUARTERS_CITY, booking.getWorkplace().getWorkspace().getHeadquarters().getCity(),
                        EmailMessageParser.Placeholder.HEADQUARTERS_ADDRESS, booking.getWorkplace().getWorkspace().getHeadquarters().getAddress(),
                        EmailMessageParser.Placeholder.BOOKING_DATE, booking.getBookingDate().toString(),
                        EmailMessageParser.Placeholder.WORKSPACE_NAME, booking.getWorkplace().getWorkspace().getName(),
                        EmailMessageParser.Placeholder.WORKSPACE_FLOOR, booking.getWorkplace().getWorkspace().getFloor(),
                        EmailMessageParser.Placeholder.WORKPLACE_NAME, booking.getWorkplace().getName()
                )
        );
        // Sending email
        this.emailNotificationService.notify(
                booking.getWorker().getEmail(),
                WORKPLACE_BOOKING_NOTIFICATION_TITLE,
                message
        );
    }


    /**
     * Internal method describing the logic after which notifying the observers of a given headquarters when more than
     * 65% of workplaces are booked on a given date.
     *
     * @param bookingDate  the date
     * @param headquarters the headquarters changing its state
     */
    private void notifyHeadquartersObservers(final LocalDate bookingDate, final Headquarters headquarters) {
        final Pair<Long, Long> availableOnTotalWorkplaces = this.workplaceService
                .countAvailableOnTotalForHeadquarters(headquarters, bookingDate);
        final float availablePercentage = 100f * availableOnTotalWorkplaces.getFirst() / availableOnTotalWorkplaces.getSecond();

        if (availablePercentage < 35) {
            log.info("Workplaces running out: headquarters {} observers will be notified", headquarters.getId());

            final Context context = Context.builder()
                    .availableOnTotalWorkplaces(availableOnTotalWorkplaces)
                    .onDate(bookingDate)
                    .alreadyBookedWorkers(this.workersOn(bookingDate, headquarters))
                    .build();
            this.headquartersService.notifyObservers(headquarters, this.headquartersWorkplaceBookingsObserverService, context);
        }
    }

}
