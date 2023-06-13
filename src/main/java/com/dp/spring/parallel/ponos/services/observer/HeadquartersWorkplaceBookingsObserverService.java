package com.dp.spring.parallel.ponos.services.observer;

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
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

/**
 * Observer business logic methods to react to 75
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HeadquartersWorkplaceBookingsObserverService
        implements ObserverService<User, Headquarters, HeadquartersWorkplaceBookingsObserverService.Context> {

    private final EmailNotificationService emailNotificationService;

    private static final String FEW_HEADQUARTERS_WORKPLACES_LEFT_TITLE = "Le postazioni disponibili stanno terminando!";
    private static final String FEW_HEADQUARTERS_WORKPLACES_LEFT_MESSAGE_PATH = "email/few-headquarters-workplaces-left-template.html";


    @Override
    public void react(User observer, Headquarters publisher, Context context) {
        if (context.getAlreadyBookedWorkers().contains(observer)) {
            log.info("Observer {} do not need to be notified for \"{}\"", observer.getEmail(), FEW_HEADQUARTERS_WORKPLACES_LEFT_TITLE);
            return;
        }

        log.info("Observer {} will be notified for \"{}\"", observer.getEmail(), FEW_HEADQUARTERS_WORKPLACES_LEFT_TITLE);

        // Building message to confirm password changing
        String message = this.emailNotificationService.buildMessage(
                FEW_HEADQUARTERS_WORKPLACES_LEFT_MESSAGE_PATH,
                Map.of(
                        EmailMessageParser.Placeholder.FIRST_NAME, observer.getFirstName(),
                        EmailMessageParser.Placeholder.LAST_NAME, observer.getLastName(),
                        EmailMessageParser.Placeholder.COMPANY_NAME, publisher.getCompany().getName(),
                        EmailMessageParser.Placeholder.HEADQUARTERS_CITY, publisher.getCity(),
                        EmailMessageParser.Placeholder.HEADQUARTERS_ADDRESS, publisher.getAddress(),
                        EmailMessageParser.Placeholder.BOOKING_DATE, context.getOnDate().toString(),
                        EmailMessageParser.Placeholder.AVAILABLE_WORKPLACES, context.getAvailableOnTotalWorkplaces().getFirst().toString(),
                        EmailMessageParser.Placeholder.TOTAL_WORKPLACES, context.getAvailableOnTotalWorkplaces().getSecond().toString()
                )
        );

        // Sending email
        this.emailNotificationService.notify(
                observer.getEmail(),
                FEW_HEADQUARTERS_WORKPLACES_LEFT_TITLE,
                message
        );
    }

    @Builder
    @Value
    public static class Context {
        @NotNull Pair<Long, Long> availableOnTotalWorkplaces;
        @NotNull LocalDate onDate;
        @NotNull Set<User> alreadyBookedWorkers;
    }
}
