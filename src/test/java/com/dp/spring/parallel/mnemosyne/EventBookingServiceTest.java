package com.dp.spring.parallel.mnemosyne;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.agora.services.EventService;
import com.dp.spring.parallel.common.exceptions.EventBookingAlreadyExistsForWorker;
import com.dp.spring.parallel.common.exceptions.EventNoPlacesAvailableException;
import com.dp.spring.parallel.common.exceptions.EventNotBookableException;
import com.dp.spring.parallel.common.exceptions.WorkplaceBookingCancellationNotValidException;
import com.dp.spring.parallel.common.fixtures.EventBookingFixture;
import com.dp.spring.parallel.common.fixtures.EventFixture;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.mnemosyne.database.entities.EventBooking;
import com.dp.spring.parallel.mnemosyne.database.repositories.EventBookingRepository;
import com.dp.spring.parallel.mnemosyne.services.impl.EventBookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventBookingServiceTest {

    @Mock
    EmailNotificationService emailNotificationService;
    @Spy
    HeadquartersService headquartersService;
    @Spy
    EventService eventService;

    @Spy
    EventBookingRepository eventBookingRepository;

    @InjectMocks
    @Spy
    EventBookingServiceImpl eventBookingService;


    // Mock
    void mockGetPrincipal(User user) {
        doReturn(user)
                .when(eventBookingService)
                .getPrincipalOrThrow();
    }


    @BeforeEach
    public void setUp() {
    }


    @Test
    void eventBookingsFromDate_shouldWork() {
        LocalDate fromDate = LocalDate.now().minusYears(3);

        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(List.of(EventFixture.get()))
                .when(eventBookingRepository)
                .findAllByWorkerAndEventOnDateGreaterThanEqualOrderByEventOnDate(any(User.class), eq(fromDate));

        var result = eventBookingService.eventBookingsFromDate(fromDate);

        assertEquals(1, result.size(), "return not coherent");
    }

    @Test
    void book_whenPastDate_shouldThrow() {
        Integer headquartersId = 2;
        Integer eventId = 10;

        doReturn(EventFixture.getPast())
                .when(eventService)
                .event(headquartersId, eventId);
        mockGetPrincipal(UserFixture.getCompanyManager());

        assertThrows(EventNotBookableException.class, () -> eventBookingService.book(headquartersId, eventId));
    }

    @Test
    void book_whenAlreadyBooked_shouldThrow() {
        Integer headquartersId = 2;
        Integer eventId = 10;

        doReturn(EventFixture.get())
                .when(eventService)
                .event(headquartersId, eventId);
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(true)
                .when(eventBookingRepository)
                .existsByWorkerAndEvent(any(User.class), any(Event.class));

        assertThrows(EventBookingAlreadyExistsForWorker.class, () -> eventBookingService.book(headquartersId, eventId));
    }

    @Test
    void book_whenSoldOut_shouldThrow() {
        Integer headquartersId = 2;
        Integer eventId = 10;

        Event event = EventFixture.get();
        doReturn(event)
                .when(eventService)
                .event(headquartersId, eventId);
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(false)
                .when(eventBookingRepository)
                .existsByWorkerAndEvent(any(User.class), any(Event.class));
        doReturn(event.getMaxPlaces().longValue())
                .when(eventBookingRepository)
                .countByEvent(any(Event.class));

        assertThrows(EventNoPlacesAvailableException.class, () -> eventBookingService.book(headquartersId, eventId));
    }

    @Test
    void book_whenOk_shouldWork() {
        Integer headquartersId = 2;
        Integer eventId = 10;

        doReturn(EventFixture.get())
                .when(eventService)
                .event(headquartersId, eventId);
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(false)
                .when(eventBookingRepository)
                .existsByWorkerAndEvent(any(User.class), any(Event.class));
        doReturn(0L)
                .when(eventBookingRepository)
                .countByEvent(any(Event.class));
        doReturn(EventBookingFixture.get())
                .when(eventBookingRepository)
                .save(any(EventBooking.class));

        eventBookingService.book(headquartersId, eventId);

        verify(eventBookingRepository).save(any(EventBooking.class));
    }

    @Test
    void cancel_whenNotFound_shouldThrowAndIgnored() {
        Integer headquartersId = 2;
        Integer eventId = 10;
        Integer bookingId = 20;

        doReturn(EventFixture.get())
                .when(eventService)
                .event(headquartersId, eventId);
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(Optional.empty())
                .when(eventBookingRepository)
                .findByIdAndEvent(anyInt(), any(Event.class));

        assertDoesNotThrow(() -> eventBookingService.cancel(headquartersId, eventId, bookingId));
    }

    @Test
    void cancel_whenOthersBooking_shouldThrow() {
        Integer headquartersId = 2;
        Integer eventId = 10;
        Integer bookingId = 20;

        doReturn(EventFixture.get())
                .when(eventService)
                .event(headquartersId, eventId);
        mockGetPrincipal(UserFixture.getEmployee());
        doReturn(ofNullable(EventBookingFixture.get()))
                .when(eventBookingRepository)
                .findByIdAndEvent(anyInt(), any(Event.class));

        assertThrows(AccessDeniedException.class, () -> eventBookingService.cancel(headquartersId, eventId, bookingId));
    }

    @Test
    void cancel_whenPastBooking_shouldThrow() {
        Integer headquartersId = 2;
        Integer eventId = 10;
        Integer bookingId = 20;

        doReturn(EventFixture.getPast())
                .when(eventService)
                .event(headquartersId, eventId);
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(ofNullable(EventBookingFixture.getPast()))
                .when(eventBookingRepository)
                .findByIdAndEvent(anyInt(), any(Event.class));

        assertThrows(WorkplaceBookingCancellationNotValidException.class, () -> eventBookingService.cancel(headquartersId, eventId, bookingId));
    }

    @Test
    void cancel_whenOk_shouldWork() {
        Integer headquartersId = 2;
        Integer eventId = 10;
        Integer bookingId = 20;

        doReturn(EventFixture.get())
                .when(eventService)
                .event(headquartersId, eventId);
        mockGetPrincipal(UserFixture.getCompanyManager());
        doReturn(ofNullable(EventBookingFixture.get()))
                .when(eventBookingRepository)
                .findByIdAndEvent(anyInt(), any(Event.class));

        assertDoesNotThrow(() -> eventBookingService.cancel(headquartersId, eventId, bookingId));
    }

    @Test
    void cancelAll_shouldWork() {
        Event event = EventFixture.get();

        doReturn(List.of(EventBookingFixture.get()))
                .when(eventBookingRepository)
                .findAllByEvent(eq(event));
        doNothing().when(eventBookingRepository).softDelete(any(EventBooking.class));

        eventBookingService.cancelAll(event);

        verify(eventBookingRepository).softDelete(any(EventBooking.class));

    }

    @Test
    void countAvailablePlaces_shouldWork() {
        Event event = EventFixture.get();

        doReturn(2L)
                .when(eventBookingRepository)
                .countByEvent(event);

        var available = eventBookingService.countAvailablePlaces(event);

        assertEquals(event.getMaxPlaces() - 2L, available, "inconsistent subtraction");
    }

}
