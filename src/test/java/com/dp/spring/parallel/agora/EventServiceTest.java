package com.dp.spring.parallel.agora;

import com.dp.spring.parallel.agora.api.dto.CreateUpdateEventRequestDTO;
import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.agora.database.repositories.EventRepository;
import com.dp.spring.parallel.agora.services.impl.EventServiceImpl;
import com.dp.spring.parallel.agora.services.observer.HeadquartersEventsObserverService;
import com.dp.spring.parallel.common.fixtures.EventFixture;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    HeadquartersService headquartersService;
    @Mock
    EventBookingService eventBookingService;

    @Mock
    CrudRepository<Headquarters, Integer> crudRepository;
    @Spy
    EventRepository eventRepository;
    @Mock
    HeadquartersRepository headquartersRepository;

    @Mock
    EmailNotificationService emailNotificationService;
    @Spy
    HeadquartersEventsObserverService headquartersEventsObserverService = new HeadquartersEventsObserverService(emailNotificationService);


    @InjectMocks
    @Spy
    EventServiceImpl eventService = new EventServiceImpl(headquartersService, eventBookingService, eventRepository, headquartersEventsObserverService);


    // Mock
    void mockLoggedUser(User user) {
        doReturn(user)
                .when(eventService)
                .getPrincipalOrThrow();
    }

    @BeforeEach
    public void setUp() {
    }


    @Test
    void add_() {
        Integer headquartersId = 1;
        CreateUpdateEventRequestDTO request = CreateUpdateEventRequestDTO.builder()
                .name("event")
                .eventDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(2))
                .maxPlaces(30)
                .build();

        // mock fasfs

        mockLoggedUser(UserFixture.getCompanyManager());

        doReturn(EventFixture.get())
                .when(eventRepository)
                .save(any(Event.class));

        Event created = eventService.add(headquartersId, request);

        verify(eventRepository).save(eq(created));

        verify(headquartersEventsObserverService)
                .react(any(User.class), any(Headquarters.class), any(HeadquartersEventsObserverService.Context.class));
    }

}
