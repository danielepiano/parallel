package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.common.exceptions.EmailAlreadyExistsException;
import com.dp.spring.parallel.common.utils.ResourcesUtils;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.utils.RandomPasswordUtils;
import com.dp.spring.parallel.mnemosyne.services.notification.EmailNotificationService;
import com.dp.spring.parallel.mnemosyne.utils.EmailMessageParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.dp.spring.parallel.mnemosyne.utils.EmailMessageParser.Keyword.*;

/**
 * Template pattern for the registration service, that should be implemented in different strategies.
 *
 * @param <T> a subclass of {@link User}
 */
@Service
public abstract class RegistrationService<T extends User> {
    @Autowired
    protected EmailNotificationService emailNotificationService;

    @Autowired
    protected UserRepository<User> userRepository;
    @Autowired
    protected UserRepository<T> scopedUserRepository; // @todo is it necessary??

    @Autowired
    protected PasswordEncoder passwordEncoder;

    private static final String DEFAULT_NOTIFICATION_TITLE = "Benvenuto in Parallel, " + FIRST_NAME.getTemplate() + "!";
    private static final String DEFAULT_NOTIFICATION_MESSAGE_PATH = "email/first-access-credentials-template.html";


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
    protected abstract T buildUser(final String encodedPassword, final Integer scopeId, final RegistrationRequestDTO dto);


    /**
     * 3rd step of the {@link #register(Integer, RegistrationRequestDTO)} template:
     * saving user in the database.
     * <br>
     * The step is overridable.
     *
     * @param user the user to be saved in the database
     */
    protected void save(final T user) {
        this.scopedUserRepository.save(user);
    }


    /**
     * 4th step of the {@link #register(Integer, RegistrationRequestDTO)} template:
     * building notification message (default: email message).
     * <br>
     * The step is overridable.
     *
     * @param generatedPassword the encoded password generated in the 1st step
     * @param user              the user saved in the database
     */
    protected String buildNotificationMessage(final String generatedPassword, final T user) {
        // Reading email message and http template from file
        String rawMessage = ResourcesUtils.readFileAsString(DEFAULT_NOTIFICATION_MESSAGE_PATH);

        // Parsing the message, replacing keywords in curly brackets with proper values
        return EmailMessageParser.parse(
                rawMessage,
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
        this.emailNotificationService.notify(
                user.getEmail(),
                EmailMessageParser.parse(DEFAULT_NOTIFICATION_TITLE, Map.of(FIRST_NAME, user.getFirstName())),
                message
        );
    }
}
