package com.dp.spring.parallel.agora.api.controllers;

import com.dp.spring.parallel.agora.api.dto.CreateUpdateEventRequestDTO;
import com.dp.spring.parallel.agora.api.dto.EventResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.ROLE_ADMIN_VALUE;
import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.ROLE_COMPANY_MANAGER_VALUE;

@RequestMapping("/api/v1")
public interface EventsController {

    @PostMapping(path = "/headquarters/{headquartersId}/events", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    EventResponseDTO addEvent(
            @PathVariable("headquartersId") Integer headquartersId,
            @Valid @RequestBody CreateUpdateEventRequestDTO createRequest
    );

    @GetMapping(path = "/headquarters/{headquartersId}/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    EventResponseDTO event(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("eventId") Integer eventId
    );

    @GetMapping(path = "/headquarters/{headquartersId}/events", produces = MediaType.APPLICATION_JSON_VALUE)
    List<EventResponseDTO> events(
            @PathVariable("headquartersId") Integer headquartersId
    );

    @GetMapping(path = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
    List<EventResponseDTO> events();

    @PutMapping(path = "/headquarters/{headquartersId}/events/{eventId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    EventResponseDTO updateEvent(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("eventId") Integer eventId,
            @Valid @RequestBody CreateUpdateEventRequestDTO updateRequest
    );

    @DeleteMapping(path = "/headquarters/{headquartersId}/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeEvent(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("eventId") Integer eventId
    );

}
