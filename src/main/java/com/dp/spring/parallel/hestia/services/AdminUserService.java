package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.common.exceptions.UserNotDeletableException;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.AdminUserRepository;
import com.dp.spring.parallel.hestia.services.registration_strategies.AdminRegistrationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dp.spring.parallel.common.exceptions.UserNotDeletableException.AT_LEAST_ONE_ADMIN_CONFLICT;

/**
 * Generic {@link User} services.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService extends UserService {
    private final AdminRegistrationStrategy adminRegistrationStrategy;
    private final AdminUserRepository adminUserRepository;


    public void register(
            final RegistrationRequestDTO toRegister
    ) {
        super.register(toRegister, adminRegistrationStrategy);
    }


    public void disable(
            final Integer userId
    ) {
        // At least one admin should be active
        if (this.adminUserRepository.findAll().size() < 2) {
            throw new UserNotDeletableException(AT_LEAST_ONE_ADMIN_CONFLICT);
        }
        this.adminUserRepository.softDeleteById(userId);
    }

}
