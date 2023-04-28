package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.common.exceptions.EmailAlreadyExistsException;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.utils.RandomPasswordUtils;
import com.dp.spring.parallel.mnemosyne.services.email.EmailService;
import lombok.RequiredArgsConstructor;

/**
 * Template pattern for the registration service, that should be implemented in different strategies.
 *
 * @param <T> a subclass of {@link User}
 */
@RequiredArgsConstructor
public abstract class RegistrationService<T extends User> {
    protected final UserRepository<User> userRepository;
    protected final UserRepository<T> scopedUserRepository;
    protected final EmailService emailService;


    /**
     * Template for the registration process. Steps are described in the respective methods.
     *
     * @param scopeId the id of the resource to attach the user to (e.g. company id, headquarters id, etc.)
     * @param dto     the data of the user to be registered
     */
    public void register(final Integer scopeId, final RegistrationRequestDTO dto) {
        // @todo
        // - definizione metodo abstract check
        // CompanyManager -> [Admin] company esistente
        //                   [CompanyManager] company esistente e stessa del CompanyManager
        // HeadqsRecep -> [Admin] headqs esistente
        //                [CompanyManager] headqs esistente e appartenente a company del CompanyManager
        // Employee -> [Admin] company esistente
        //             [CompanyManager] company esistente e stessa del CompanyManager

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        final String generatedPassword = this.generatePassword();
        T user = this.buildUser(generatedPassword, scopeId, dto);
        this.save(user);
        this.sendNotification(user);
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
     * @param generatedPassword the password generated in the 1st step
     * @param scopeId           the id of the resource to attach the user to (e.g. company id, headquarters id, etc.)
     * @param dto               the data of the user to be registered
     * @return the details of the user to be registered
     */
    protected abstract T buildUser(final String generatedPassword, final Integer scopeId, final RegistrationRequestDTO dto);


    /**
     * 3rd step of the {@link #register(Integer, RegistrationRequestDTO)} template:
     * saving user in the database.
     * <br>
     * The step is overridable.
     *
     * @param user the user entity to be saved in the database
     */
    protected void save(final T user) {
        this.scopedUserRepository.save(user);
    }


    /**
     * 4th step of the {@link #register(Integer, RegistrationRequestDTO)} template:
     * sending notification (default email) following the successful registration.
     */
    protected void sendNotification(final T user) {

    }
}
