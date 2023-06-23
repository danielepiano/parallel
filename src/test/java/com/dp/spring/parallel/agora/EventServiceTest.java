package com.dp.spring.parallel.agora;

import com.dp.spring.parallel.agora.api.dto.CreateUpdateEventRequestDTO;
import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.agora.database.repositories.EventRepository;
import com.dp.spring.parallel.agora.services.impl.EventServiceImpl;
import com.dp.spring.parallel.agora.services.observer.HeadquartersEventsObserverService;
import com.dp.spring.parallel.common.exceptions.EventNotFoundException;
import com.dp.spring.parallel.common.fixtures.EventFixture;
import com.dp.spring.parallel.common.fixtures.HeadquartersFixture;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.repositories.HeadquartersRepository;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.mnemosyne.services.EventBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Spy
    HeadquartersService headquartersService;
    @Spy
    EventBookingService eventBookingService;

    @Spy
    CrudRepository<Headquarters, Integer> crudRepository;
    @Spy
    EventRepository eventRepository;
    @Spy
    HeadquartersRepository headquartersRepository;

    @Mock
    EmailNotificationService emailNotificationService;
    @Spy
    HeadquartersEventsObserverService headquartersEventsObserverService = new HeadquartersEventsObserverService(emailNotificationService);


    @InjectMocks
    @Spy
    EventServiceImpl eventService;


    // Mock
    void mockLoggedUser(User user) {
        doReturn(user)
                .when(eventService)
                .getPrincipalOrThrow();
    }

    void mockGetAndCheckHeadquarters() {
        doReturn(HeadquartersFixture.getWithObservers())
                .when(headquartersService)
                .headquarters(anyInt());
        mockLoggedUser(UserFixture.getCompanyManager());
    }

    @BeforeEach
    public void setUp() {
    }


    @Test
    void add_shouldWork() {
        Integer headquartersId = 1;
        CreateUpdateEventRequestDTO request = CreateUpdateEventRequestDTO.builder()
                .name("event")
                .eventDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(2))
                .maxPlaces(30)
                .build();

        mockGetAndCheckHeadquarters();
        doReturn(EventFixture.get())
                .when(eventRepository)
                .save(any(Event.class));
        doNothing().when(headquartersService).notifyObservers(
                any(Headquarters.class), eq(headquartersEventsObserverService), any(HeadquartersEventsObserverService.Context.class));

        eventService.add(headquartersId, request);

        verify(eventRepository).save(any(Event.class));
        verify(headquartersService)
                .notifyObservers(any(Headquarters.class), eq(headquartersEventsObserverService), any(HeadquartersEventsObserverService.Context.class));
    }


    @Test
    void event_whenNotFound_shouldThrow() {
        Integer headquartersId = 2;
        Integer eventId = 10;

        doReturn(HeadquartersFixture.get())
                .when(headquartersService)
                .headquarters(anyInt());
        doReturn(Optional.empty())
                .when(eventRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));

        assertThrows(EventNotFoundException.class, () -> eventService.event(headquartersId, eventId));
    }

    @Test
    void event_whenFound_shouldWork() {
        Integer headquartersId = 2;
        Integer eventId = 10;

        doReturn(HeadquartersFixture.get())
                .when(headquartersService)
                .headquarters(anyInt());
        doReturn(ofNullable(EventFixture.get()))
                .when(eventRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));

        assertDoesNotThrow(() -> eventService.event(headquartersId, eventId));
    }

    @Test
    void events_shouldWork() {
        Integer headquartersId = 2;

        doReturn(HeadquartersFixture.get())
                .when(headquartersService)
                .headquarters(anyInt());
        doReturn(List.of(EventFixture.get()))
                .when(eventRepository)
                .findAllByHeadquarters(any(Headquarters.class));

        var result = eventService.events(headquartersId);
        assertEquals(1, result.size(), "return not coherent");
    }

    @Test
    void eventsFromDate_shouldWork() {
        LocalDate fromDate = LocalDate.now().minusYears(3);

        Event event = EventFixture.get();
        doReturn(List.of(event))
                .when(eventRepository)
                .findAllByEventDateGreaterThanEqual(any(LocalDate.class));

        var result = eventService.eventsFromDate(fromDate);

        assertEquals(1, result.size(), "return not coherent");
        assertTrue(result.get(0).getOnDate().isAfter(fromDate));
    }

    @Test
    void update_whenOk_shouldWork() {
        Integer headquartersId = 1;
        Integer eventId = 10;
        CreateUpdateEventRequestDTO request = CreateUpdateEventRequestDTO.builder()
                .name("event")
                .eventDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(2))
                .maxPlaces(30)
                .build();

        mockGetAndCheckHeadquarters();
        doReturn(ofNullable(EventFixture.get()))
                .when(eventRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));
        doReturn(EventFixture.get())
                .when(eventRepository)
                .save(any(Event.class));

        eventService.update(headquartersId, eventId, request);

        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void update_whenNotFound_shouldThrow() {
        Integer headquartersId = 1;
        Integer eventId = 10;
        CreateUpdateEventRequestDTO request = CreateUpdateEventRequestDTO.builder()
                .name("event")
                .eventDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(2))
                .maxPlaces(30)
                .build();

        mockGetAndCheckHeadquarters();
        doReturn(Optional.empty())
                .when(eventRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));

        assertThrows(EventNotFoundException.class, () -> eventService.update(headquartersId, eventId, request));
    }

    @Test
    void remove_whenFound_shouldWork() {
        Integer headquartersId = 1;
        Integer eventId = 10;

        mockGetAndCheckHeadquarters();
        doReturn(ofNullable(EventFixture.get()))
                .when(eventRepository)
                .findByIdAndHeadquarters(anyInt(), any(Headquarters.class));
        doNothing().when(eventBookingService).cancelAll(any(Event.class));
        doNothing().when(eventRepository).softDelete(any(Event.class));

        eventService.remove(headquartersId, eventId);

        verify(eventBookingService).cancelAll(any(Event.class));
        verify(eventRepository).softDelete(any(Event.class));
    }

    @Test
    void removeAll_shouldWork() {
        Headquarters headquarters = HeadquartersFixture.get();

        doReturn(List.of(EventFixture.get()))
                .when(eventRepository)
                .findAllByHeadquarters(any(Headquarters.class));
        doNothing().when(eventBookingService).cancelAll(any(Event.class));
        doNothing().when(eventRepository).softDelete(any(Event.class));

        eventService.removeAll(headquarters);

        verify(eventBookingService).cancelAll(any(Event.class));
        verify(eventRepository).softDelete(any(Event.class));
    }

}
