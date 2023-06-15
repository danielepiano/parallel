package com.dp.spring.parallel.agora.services;

import com.dp.spring.parallel.agora.api.dto.CreateUpdateEventRequestDTO;
import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    /**
     * Adding an event.
     *
     * @param headquartersId the id of the headquarters where the event take place
     * @param createRequest  the event creation request
     * @return the created event
     */
    Event add(
            Integer headquartersId,
            CreateUpdateEventRequestDTO createRequest
    );

    /**
     * Retrieving an event given its id and the id of the headquarters it takes place.
     *
     * @param headquartersId the id of the headquarters
     * @param eventId        the id of the event to delete
     * @return the event
     */
    Event event(
            Integer headquartersId,
            Integer eventId
    );

    /**
     * Retrieving all the events arranged in the headquarters of given id.
     *
     * @param headquartersId the id of the headquarters
     * @return the events
     */
    List<Event> events(
            Integer headquartersId
    );

    /**
     * Retrieving all events from a given date.
     *
     * @param fromDate the date
     * @return the future events
     */
    List<Event> eventsFromDate(
            LocalDate fromDate
    );

    /**
     * Updating an event.
     *
     * @param headquartersId the id of the headquarters where the event take place
     * @param eventId        the id of the event to delete
     * @param updateRequest  the event update request
     * @return the updated workspace
     */
    Event update(
            Integer headquartersId,
            Integer eventId,
            CreateUpdateEventRequestDTO updateRequest
    );

    /**
     * Removing an event.
     *
     * @param headquartersId the id of the headquarters where the event take place
     * @param eventId        the id of the event to delete
     */
    void remove(
            Integer headquartersId,
            Integer eventId
    );

    /**
     * Removing all the event arranged in the given headquarters.
     *
     * @param headquarters the headquarters of which delete the events
     */
    void removeAll(
            Headquarters headquarters
    );

}
