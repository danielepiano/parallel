package com.dp.spring.parallel.agora.api.controllers.impl;

import com.dp.spring.parallel.agora.api.controllers.EventsController;
import com.dp.spring.parallel.agora.api.dto.CreateUpdateEventRequestDTO;
import com.dp.spring.parallel.agora.api.dto.EventResponseDTO;
import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.agora.services.EventService;
import com.dp.spring.parallel.mnemosyne.services.EventBookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
public class EventControllerImpl implements EventsController {

    private final EventService eventService;
    private final EventBookingService eventBookingService;


    @Override
    public EventResponseDTO addEvent(Integer headquartersId, CreateUpdateEventRequestDTO createRequest) {
        final Event event = this.eventService.add(headquartersId, createRequest);
        return EventResponseDTO.of(event);
    }

    @Override
    public EventResponseDTO event(Integer headquartersId, Integer eventId) {
        final Event event = this.eventService.event(headquartersId, eventId);
        final long availablePlaces = event.getMaxPlaces() - this.eventBookingService.count(event);
        return EventResponseDTO.of(availablePlaces, event);
    }

    @Override
    public List<EventResponseDTO> events(Integer headquartersId) {
        return this.eventService.events(headquartersId).stream()
                .map(event -> {
                    final long availablePlaces = event.getMaxPlaces() - this.eventBookingService.count(event);
                    return EventResponseDTO.of(availablePlaces, event);
                }).toList();
    }

    @Override
    public List<EventResponseDTO> events() {
        return this.eventService.eventsFromDate(LocalDate.now()).stream()
                .map(event -> {
                    final long availablePlaces = event.getMaxPlaces() - this.eventBookingService.count(event);
                    return EventResponseDTO.of(availablePlaces, event);
                }).toList();
    }

    @Override
    public EventResponseDTO updateEvent(Integer headquartersId, Integer eventId, CreateUpdateEventRequestDTO updateRequest) {
        final Event event = this.eventService.update(headquartersId, eventId, updateRequest);
        final long availablePlaces = event.getMaxPlaces() - this.eventBookingService.count(event);
        return EventResponseDTO.of(availablePlaces, event);
    }

    @Override
    public void removeEvent(Integer headquartersId, Integer eventId) {
        this.eventService.remove(headquartersId, eventId);
    }
}
