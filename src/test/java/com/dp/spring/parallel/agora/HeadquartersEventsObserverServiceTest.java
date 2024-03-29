package com.dp.spring.parallel.agora;

import com.dp.spring.parallel.common.fixtures.HeadquartersFixture;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.mnemosyne.services.observer.HeadquartersWorkplaceBookingsObserverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HeadquartersEventsObserverServiceTest {

    @Mock
    EmailNotificationService emailNotificationService;

    @InjectMocks
    @Spy
    HeadquartersWorkplaceBookingsObserverService headquartersWorkplaceBookingsObserverService;


    @BeforeEach
    public void setUp() {
    }


    @Test
    void react_shouldWork() {
        User observer = UserFixture.getCompanyManager();
        Headquarters headquarters = HeadquartersFixture.get();
        HeadquartersWorkplaceBookingsObserverService.Context context = HeadquartersWorkplaceBookingsObserverService.Context.builder()
                .availableOnTotalWorkplaces(Pair.of(10L, 30L))
                .onDate(LocalDate.now().plusDays(14))
                .alreadyBookedWorkers(Set.of(UserFixture.getEmployee()))
                .build();

        doReturn("message")
                .when(emailNotificationService)
                .buildMessage(anyString(), anyMap());
        doNothing().when(emailNotificationService).notify(any(), anyString(), any());

        headquartersWorkplaceBookingsObserverService.react(observer, headquarters, context);

        verify(emailNotificationService).buildMessage(anyString(), anyMap());
        verify(emailNotificationService).notify(any(), anyString(), any());
    }

    @Test
    void react_whenAlreadyBooked_shouldDoNothing() {
        User observer = UserFixture.getCompanyManager();
        Headquarters headquarters = HeadquartersFixture.get();
        HeadquartersWorkplaceBookingsObserverService.Context context = HeadquartersWorkplaceBookingsObserverService.Context.builder()
                .availableOnTotalWorkplaces(Pair.of(10L, 30L))
                .onDate(LocalDate.now().plusDays(14))
                .alreadyBookedWorkers(Set.of(UserFixture.getCompanyManager(), UserFixture.getEmployee()))
                .build();

        headquartersWorkplaceBookingsObserverService.react(observer, headquarters, context);

        verify(emailNotificationService, times(0)).buildMessage(anyString(), anyMap());
        verify(emailNotificationService, times(0)).notify(any(), anyString(), any());
    }

}
