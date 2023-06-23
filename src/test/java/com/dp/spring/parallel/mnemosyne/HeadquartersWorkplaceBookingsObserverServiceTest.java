package com.dp.spring.parallel.mnemosyne;

import com.dp.spring.parallel.agora.services.observer.HeadquartersEventsObserverService;
import com.dp.spring.parallel.common.fixtures.EventFixture;
import com.dp.spring.parallel.common.fixtures.HeadquartersFixture;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.database.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HeadquartersWorkplaceBookingsObserverServiceTest {

    @Mock
    EmailNotificationService emailNotificationService;

    @InjectMocks
    @Spy
    HeadquartersEventsObserverService headquartersEventsObserverService;


    @BeforeEach
    public void setUp() {
    }


    @Test
    void react_shouldWork() {
        User observer = UserFixture.getCompanyManager();
        Headquarters headquarters = HeadquartersFixture.get();
        HeadquartersEventsObserverService.Context context = HeadquartersEventsObserverService.Context.builder()
                .createdEvent(EventFixture.get())
                .build();

        doReturn("message")
                .when(emailNotificationService)
                .buildMessage(anyString(), anyMap());
        doNothing().when(emailNotificationService).notify(any(), anyString(), any());

        headquartersEventsObserverService.react(observer, headquarters, context);

        verify(emailNotificationService).buildMessage(anyString(), anyMap());
        verify(emailNotificationService).notify(any(), anyString(), any());
    }

}
