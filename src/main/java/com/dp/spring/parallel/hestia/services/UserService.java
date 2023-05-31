package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.hestia.api.dtos.ChangePasswordRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.UpdatePersonalDataRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.User;

/**
 * Generic {@link User} operations.
 */
public interface UserService {
    /**
     * User creation when no resource scope id is defined.<br>
     * Read also {@link #register(Integer, RegistrationRequestDTO, RegistrationService)}.
     *
     * @param toRegister          the user to register
     * @param registrationService the strategy to use for registration
     * @param <U>                 the type of the user to register, related to its role
     */
    <U extends User> void register(
            final RegistrationRequestDTO toRegister,
            final RegistrationService<U> registrationService
    );

    /**
     * User creation according to the {@link RegistrationService} implementation passed as a parameter.
     *
     * @param scopeId             the id of the resource to attach the user to (e.g. company id, headquarters id, etc.)
     * @param toRegister          the user to register
     * @param registrationService the strategy to use for registration
     * @param <U>                 the type of the user to register, related to its role
     */
    <U extends User> void register(
            final Integer scopeId,
            final RegistrationRequestDTO toRegister,
            final RegistrationService<U> registrationService
    );


    /**
     * Retrieving user by id.
     *
     * @param id the id of the user
     * @return the user
     */
    User user(final Integer id);


    /**
     * Updating the authenticated user's personal data.
     *
     * @param updatedData the updated personal data
     */
    void updatePersonalData(final UpdatePersonalDataRequestDTO updatedData);


    /**
     * Changing the authenticated user's password.
     *
     * @param changeRequest the request for changing password
     */
    void changePassword(final ChangePasswordRequestDTO changeRequest);

}
