package com.dp.spring.parallel.agora.services.observer;

import com.dp.spring.parallel.agora.database.entities.Event;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hermes.utils.EmailMessageParser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.springcore.observer.ObserverService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Observer business logic methods to react to 75
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HeadquartersEventsObserverService
        implements ObserverService<User, Headquarters, HeadquartersEventsObserverService.Context> {

    private final EmailNotificationService emailNotificationService;

    private static final String NEW_HEADQUARTERS_EVENT_TITLE = "Un evento che potrebbe interessarti...";
    private static final String NEW_HEADQUARTERS_EVENT_MESSAGE_PATH = "email/new-headquarters-event-template.html";


    @Override
    public void react(User observer, Headquarters publisher, Context context) {
        log.info("Observer {} will be notified for \"{}\"", observer.getEmail(), NEW_HEADQUARTERS_EVENT_TITLE);

        // Building message to confirm password changing
        String message = this.emailNotificationService.buildMessage(
                NEW_HEADQUARTERS_EVENT_MESSAGE_PATH,
                Map.of(
                        EmailMessageParser.Placeholder.FIRST_NAME, observer.getFirstName(),
                        EmailMessageParser.Placeholder.LAST_NAME, observer.getLastName(),
                        EmailMessageParser.Placeholder.COMPANY_NAME, publisher.getCompany().getName(),
                        EmailMessageParser.Placeholder.HEADQUARTERS_CITY, publisher.getCity(),
                        EmailMessageParser.Placeholder.HEADQUARTERS_ADDRESS, publisher.getAddress(),
                        EmailMessageParser.Placeholder.EVENT_DATE, context.getCreatedEvent().getEventDate().toString(),
                        EmailMessageParser.Placeholder.EVENT_START_TIME, context.getCreatedEvent().getStartTime().toString(),
                        EmailMessageParser.Placeholder.EVENT_END_TIME, context.getCreatedEvent().getEndTime().toString(),
                        EmailMessageParser.Placeholder.EVENT_NAME, context.getCreatedEvent().getName(),
                        EmailMessageParser.Placeholder.EVENT_MAX_PLACES, context.getCreatedEvent().getMaxPlaces().toString()
                )
        );

        // Sending email
        this.emailNotificationService.notify(
                observer.getEmail(),
                NEW_HEADQUARTERS_EVENT_TITLE,
                message
        );
    }

    @Builder
    @Value
    public static class Context {
        @NotNull Event createdEvent;
    }
}
