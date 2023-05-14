package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.common.exceptions.EmailAlreadyExistsException;
import com.dp.spring.parallel.hermes.services.notification.EmailNotificationService;
import com.dp.spring.parallel.hermes.utils.EmailMessageParser;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.utils.RandomPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dp.spring.parallel.hermes.utils.EmailMessageParser.Keyword.*;

/**
 * Template pattern for the registration service, that should be implemented in different strategies.
 *
 * @param <T> a subclass of {@link User}
 */
@Service
@Transactional
public abstract class RegistrationService<T extends User> {
    @Autowired
    protected EmailNotificationService emailNotificationService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    private static final String DEFAULT_NOTIFICATION_TITLE = "Benvenuto in Parallel, " + FIRST_NAME.getTemplate() + "!";
    private static final String DEFAULT_NOTIFICATION_MESSAGE_PATH = "email/default-first-access-credentials-template.html";


    /**
     * Template for the registration process. Steps are described in the respective methods.
     *
     * @param scopeId the id of the resource to attach the user to (e.g. company id, headquarters id, etc.)
     * @param dto     the data of the user to be registered
     */
    public void register(final Integer scopeId, final RegistrationRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        final String generatedPassword = this.generatePassword();
        final String encodedPassword = this.passwordEncoder.encode(generatedPassword);
        T user = this.buildUser(encodedPassword, scopeId, dto);
        this.save(user);
        final String message = this.buildNotificationMessage(generatedPassword, user);
        this.sendNotification(message, user);
    }


    /**
     * 1st step of the {@link #register(Integer, RegistrationRequestDTO)} template:
     * password generation for the user to be created.
     * <br>
     * The step is overridable.
     *
     * @return the password for the user to be registered
     */
    protected String generatePassword() {
        return RandomPasswordUtils.generateRandomPassword();
    }


    /**
     * 2nd step of the {@link #register(Integer, RegistrationRequestDTO)} template:
     * building user details.
     * <br>
     * The step must be defined via override.
     *
     * @param encodedPassword the encoded password generated in the 1st step
     * @param scopeId         the id of the resource to attach the user to (e.g. company id, headquarters id, etc.)
     * @param dto             the data of the user to be registered
     * @return the details of the user to be registered
     */
    protected abstract T buildUser(
            final String encodedPassword,
            final Integer scopeId,
            final RegistrationRequestDTO dto
    );


    /**
     * 3rd step of the {@link #register(Integer, RegistrationRequestDTO)} template:
     * saving user in the database.
     * <br>
     * The step is overridable.
     *
     * @param user the user to be saved in the database
     */
    protected void save(final User user) {
        this.userRepository.save(user);
    }


    /**
     * 4th step of the {@link #register(Integer, RegistrationRequestDTO)} template:
     * building notification message (default: email message).
     * <br>
     * The step is overridable.
     *
     * @param generatedPassword the password as generated in the 1st step
     * @param user              the user saved in the database
     * @return the notification message
     */
    protected String buildNotificationMessage(final String generatedPassword, final T user) {
        return this.buildEmailNotificationMessageFromTemplate(DEFAULT_NOTIFICATION_MESSAGE_PATH, generatedPassword, user);
    }

    /**
     * Common method for building an email notification message based on a template file in resources.
     *
     * @param templatePath      the path of the message template file
     * @param generatedPassword the password as generated in the 1st step
     * @param user              the user saved in the database
     * @return the notification message
     */
    protected final String buildEmailNotificationMessageFromTemplate(
            final String templatePath,
            final String generatedPassword,
            final T user
    ) {
        return this.emailNotificationService.buildMessage(
                templatePath,
                Map.of(
                        FIRST_NAME, user.getFirstName(),
                        LAST_NAME, user.getLastName(),
                        EMAIL, user.getEmail(),
                        PASSWORD, generatedPassword
                )
        );
    }

    /**
     * 5th step of the {@link #register(Integer, RegistrationRequestDTO)} template:
     * sending notification (default: email notification).
     * <br>
     * The step is overridable.
     *
     * @param message the notification message to be sent
     * @param user    the user saved in the database
     */
    protected void sendNotification(final String message, final T user) {
        this.sendEmailNotification(message, user);
    }

    /**
     * Common method for sending an email notification.
     *
     * @param message the notification message to be sent
     * @param user    the user saved in the database
     */
    protected final void sendEmailNotification(final String message, final T user) {
        this.emailNotificationService.notify(
                user.getEmail(),
                EmailMessageParser.parse(DEFAULT_NOTIFICATION_TITLE, Map.of(FIRST_NAME, user.getFirstName())),
                message
        );
    }
}
