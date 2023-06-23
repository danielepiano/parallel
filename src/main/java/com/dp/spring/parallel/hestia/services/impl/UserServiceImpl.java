package com.dp.spring.parallel.hestia.services.impl;

import com.dp.spring.parallel.common.exceptions.UserNotFoundException;
import com.dp.spring.parallel.common.exceptions.WrongPasswordException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hermes.utils.EmailMessageParser;
import com.dp.spring.parallel.hestia.api.dtos.ChangePasswordRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.UpdatePersonalDataRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.services.RegistrationService;
import com.dp.spring.parallel.hestia.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * {@link UserService} implementation.
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BusinessService implements UserService {
    @Autowired
    protected EmailNotificationService emailNotificationService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;


    private static final String CHANGE_PASSWORD_NOTIFICATION_TITLE = "Password aggiornata con successo!";
    private static final String CHANGE_PASSWORD_NOTIFICATION_MESSAGE_PATH = "email/change-password-template.html";


    /**
     * {@inheritDoc}
     *
     * @param toRegister          the user to register
     * @param registrationService the strategy to use for registration
     * @param <U>                 the type of the user to register, related to its role
     */
    @Override
    public <U extends User> void register(
            final RegistrationRequestDTO toRegister,
            final RegistrationService<U> registrationService
    ) {
        this.register(null, toRegister, registrationService);
    }

    /**
     * {@inheritDoc}
     *
     * @param scopeId             the id of the resource to attach the user to (e.g. company id, headquarters id, etc.)
     * @param toRegister          the user to register
     * @param registrationService the strategy to use for registration
     * @param <U>                 the type of the user to register, related to its role
     */
    @Override
    public <U extends User> void register(
            final Integer scopeId,
            final RegistrationRequestDTO toRegister,
            final RegistrationService<U> registrationService
    ) {
        registrationService.register(scopeId, toRegister);
    }

    /**
     * {@inheritDoc}
     *
     * @param id the id of the user
     * @return the user
     */
    @Override
    public User user(final Integer id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserRole.ANY.getRole(), id));
    }

    /**
     * {@inheritDoc}
     *
     * @return the principal
     */
    @Override
    public User whoAmI() {
        return getPrincipalOrThrow();
    }

    /**
     * {@inheritDoc}
     *
     * @param updatedData the updated personal data
     */
    @Override
    public void updatePersonalData(
            final UpdatePersonalDataRequestDTO updatedData
    ) {
        final User principal = getPrincipalOrThrow();

        principal.setPhoneNumber(updatedData.getPhoneNumber());
        principal.setCity(updatedData.getCity());
        principal.setAddress(updatedData.getAddress());

        this.userRepository.save(principal);
    }


    /**
     * {@inheritDoc}
     *
     * @param changeRequest the request for changing password
     */
    @Override
    public void changePassword(
            final ChangePasswordRequestDTO changeRequest
    ) {
        final User principal = getPrincipalOrThrow();

        // Checking if current password specified is actually the authenticated user's password
        if (!this.passwordEncoder.matches(changeRequest.getCurrent(), principal.getPassword())) {
            throw new WrongPasswordException();
        }

        principal.setPassword(passwordEncoder.encode(changeRequest.getUpdated()));
        this.userRepository.save(principal);

        // Building message to confirm password changing
        final String message = this.emailNotificationService.buildMessage(
                CHANGE_PASSWORD_NOTIFICATION_MESSAGE_PATH,
                Map.of(EmailMessageParser.Placeholder.FIRST_NAME, principal.getFirstName())
        );
        // Sending email
        this.emailNotificationService.notify(
                principal.getEmail(),
                CHANGE_PASSWORD_NOTIFICATION_TITLE,
                message
        );
    }

}
