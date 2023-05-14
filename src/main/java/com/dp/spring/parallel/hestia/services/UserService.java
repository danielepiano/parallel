package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.common.exceptions.UserNotFoundException;
import com.dp.spring.parallel.common.exceptions.WrongPasswordException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hermes.services.notification.EmailNotificationService;
import com.dp.spring.parallel.hermes.utils.EmailMessageParser;
import com.dp.spring.parallel.hestia.api.dtos.ChangePasswordRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.UpdatePersonalDataRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Generic {@link User} services.
 */
@Service
@Transactional
public class UserService extends BusinessService {
    @Autowired
    protected EmailNotificationService emailNotificationService;

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected PasswordEncoder passwordEncoder;


    private static final String CHANGE_PASSWORD_NOTIFICATION_TITLE = "Password aggiornata con successo!";
    private static final String CHANGE_PASSWORD_NOTIFICATION_MESSAGE_PATH = "email/change-password-template.html";


    /**
     * User creation when no resource scope id is defined.<br>
     * Read also {@link #register(Integer, RegistrationRequestDTO, RegistrationService)}.
     *
     * @param toRegister          the user to register
     * @param registrationService the strategy to use for registration
     * @param <U>                 the type of the user to register, related to its role
     */
    protected <U extends User> void register(
            final RegistrationRequestDTO toRegister,
            final RegistrationService<U> registrationService
    ) {
        this.register(null, toRegister, registrationService);
    }

    /**
     * User creation according to the {@link RegistrationService} implementation passed as a parameter.
     *
     * @param scopeId             the id of the resource to attach the user to (e.g. company id, headquarters id, etc.)
     * @param toRegister          the user to register
     * @param registrationService the strategy to use for registration
     * @param <U>                 the type of the user to register, related to its role
     */
    protected <U extends User> void register(
            final Integer scopeId,
            final RegistrationRequestDTO toRegister,
            final RegistrationService<U> registrationService
    ) {
        registrationService.register(scopeId, toRegister);
    }


    public User user(final Integer id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserRole.ANY.getRole(), id));
    }


    /**
     * Updating the authenticated user's personal data.
     *
     * @param updatedData the updated personal data
     */
    public void updatePersonalData(
            final UpdatePersonalDataRequestDTO updatedData
    ) {
        final User principal = super.getPrincipalOrThrow();

        principal.setBirthDate(updatedData.getBirthDate());
        principal.setPhoneNumber(updatedData.getPhoneNumber());
        principal.setCity(updatedData.getCity());
        principal.setAddress(updatedData.getAddress());

        this.userRepository.save(principal);
    }


    /**
     * Changing the authenticated user's password.
     *
     * @param changeRequest the request for changing password
     */
    public void changePassword(
            final ChangePasswordRequestDTO changeRequest
    ) {
        final User principal = super.getPrincipalOrThrow();

        // Checking if current password specified is actually the authenticated user's password
        if (!this.passwordEncoder.matches(changeRequest.getCurrent(), principal.getPassword())) {
            throw new WrongPasswordException();
        }

        principal.setPassword(passwordEncoder.encode(changeRequest.getUpdated()));
        this.userRepository.save(principal);

        // Building message to confirm password changing
        final String message = this.emailNotificationService.buildMessage(
                CHANGE_PASSWORD_NOTIFICATION_MESSAGE_PATH,
                Map.of(EmailMessageParser.Keyword.FIRST_NAME, principal.getFirstName())
        );
        // Sending email
        this.emailNotificationService.notify(
                principal.getEmail(),
                CHANGE_PASSWORD_NOTIFICATION_TITLE,
                message
        );
        // @todo send email: Password changed
    }

}
