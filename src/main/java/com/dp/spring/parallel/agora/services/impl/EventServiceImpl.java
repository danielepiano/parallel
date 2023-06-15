package com.dp.spring.parallel.agora.services.impl;

import com.dp.spring.parallel.agora.api.dto.CreateUpdateEventRequestDTO;
import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.agora.database.repositories.EventRepository;
import com.dp.spring.parallel.agora.services.EventService;
import com.dp.spring.parallel.agora.services.observer.HeadquartersEventsObserverService;
import com.dp.spring.parallel.common.exceptions.EventNotFoundException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.mnemosyne.services.EventBookingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Headquarters event operations service implementation.
 */
@Service
@Transactional
@Slf4j
public class EventServiceImpl extends BusinessService implements EventService {

    private final HeadquartersService headquartersService;
    private final EventBookingService eventBookingService;

    private final EventRepository eventRepository;


    private final HeadquartersEventsObserverService headquartersEventsObserverService;


    public EventServiceImpl(
            @Lazy HeadquartersService headquartersService,
            @Lazy EventBookingService eventBookingService,
            EventRepository eventRepository,
            HeadquartersEventsObserverService headquartersEventsObserverService
    ) {
        this.headquartersService = headquartersService;
        this.eventBookingService = eventBookingService;
        this.eventRepository = eventRepository;
        this.headquartersEventsObserverService = headquartersEventsObserverService;
    }


    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters where the event take place
     * @param createRequest  the event creation request
     * @return the created event
     */
    @Override
    public Event add(Integer headquartersId, CreateUpdateEventRequestDTO createRequest) {
        final Headquarters headquarters = this.getAndCheckHeadquartersOrThrow(headquartersId);

        Event event = new Event();
        event.setHeadquarters(headquarters)
                .setName(createRequest.getName())
                .setOnDate(createRequest.getEventDate())
                .setStartTime(createRequest.getStartTime())
                .setEndTime(createRequest.getEndTime())
                .setMaxPlaces(createRequest.getMaxPlaces());

        event = this.eventRepository.save(event);

        this.notifyHeadquartersObservers(event);

        return event;
    }

    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters
     * @param eventId        the id of the event to delete
     * @return the event
     */
    @Override
    public Event event(Integer headquartersId, Integer eventId) {
        final Headquarters headquarters = super.getHeadquartersOrThrow(headquartersId);
        return this.eventRepository.findByIdAndHeadquarters(eventId, headquarters)
                .orElseThrow(() -> new EventNotFoundException(eventId, headquartersId));
    }

    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters
     * @return the events
     */
    @Override
    public List<Event> events(Integer headquartersId) {
        final Headquarters headquarters = super.getHeadquartersOrThrow(headquartersId);
        return this.eventRepository.findAllByHeadquarters(headquarters);
    }

    /**
     * {@inheritDoc}
     *
     * @return the future events
     */
    @Override
    public List<Event> eventsFromDate(LocalDate fromDate) {
        return this.eventRepository.findAllByEventDateGreaterThanEqual(fromDate);
    }

    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters where the event take place
     * @param eventId        the id of the event to delete
     * @param updateRequest  the event update request
     * @return the updated event
     */
    @Override
    public Event update(Integer headquartersId, Integer eventId, CreateUpdateEventRequestDTO updateRequest) {
        final Headquarters headquarters = this.getAndCheckHeadquartersOrThrow(headquartersId);

        final Event toUpdate = this.eventRepository.findByIdAndHeadquarters(eventId, headquarters)
                .orElseThrow(() -> new EventNotFoundException(eventId, headquartersId))
                .setName(updateRequest.getName())
                .setOnDate(updateRequest.getEventDate())
                .setStartTime(updateRequest.getStartTime())
                .setEndTime(updateRequest.getEndTime())
                .setMaxPlaces(updateRequest.getMaxPlaces());

        return this.eventRepository.save(toUpdate);
    }

    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters where the event take place
     * @param eventId        the id of the event to delete
     */
    @Override
    public void remove(Integer headquartersId, Integer eventId) {
        final Headquarters headquarters = this.getAndCheckHeadquartersOrThrow(headquartersId);
        this.eventRepository.findByIdAndHeadquarters(eventId, headquarters)
                .ifPresent(this::softDelete);
    }

    /**
     * {@inheritDoc}
     *
     * @param headquarters the headquarters of which delete the events
     */
    @Override
    public void removeAll(Headquarters headquarters) {
        // Get all the events for the headquarters, and soft delete each of them
        this.eventRepository.findAllByHeadquarters(headquarters)
                .forEach(this::softDelete);
    }


    /**
     * Checking if the give headquarters is under the principal control. In case, returning the headquarters.
     *
     * @param headquartersId the id of the headquarters to get and check
     * @return the headquarters
     */
    Headquarters getAndCheckHeadquartersOrThrow(final Integer headquartersId) {
        final Headquarters headquarters = super.getHeadquartersOrThrow(headquartersId);
        super.checkPrincipalScopeOrThrow(headquarters.getCompany().getId());
        return headquarters;
    }

    /**
     * Internal method to soft delete an event.<br>
     * Before removing the event, deleting all related bookings.
     *
     * @param toDelete the event to be deleted
     */
    private void softDelete(final Event toDelete) {
        this.eventBookingService.cancelAll(toDelete);
        this.eventRepository.softDelete(toDelete);
    }


    /**
     * Internal method defining the context for observers reaction to execute on creation of a new event for a given
     * headquarters.
     *
     * @param event the created event
     */
    private void notifyHeadquartersObservers(final Event event) {
        log.info("Event created: headquarters {} observers will be notified", event.getHeadquarters().getId());

        final HeadquartersEventsObserverService.Context context = HeadquartersEventsObserverService.Context.builder()
                .createdEvent(event)
                .build();
        this.headquartersService.notifyObservers(event.getHeadquarters(), this.headquartersEventsObserverService, context);
    }

}
