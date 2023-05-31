package com.dp.spring.parallel.hestia.services.impl;

import com.dp.spring.parallel.common.exceptions.UserNotDeletableException;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.repositories.AdminUserRepository;
import com.dp.spring.parallel.hestia.services.AdminUserService;
import com.dp.spring.parallel.hestia.services.registration_strategies.AdminRegistrationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dp.spring.parallel.common.exceptions.UserNotDeletableException.AT_LEAST_ONE_ADMIN_CONFLICT;

/**
 * {@link AdminUserService} implementation.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl extends UserServiceImpl implements AdminUserService {
    private final AdminRegistrationStrategy adminRegistrationStrategy;
    private final AdminUserRepository adminUserRepository;


    /**
     * {@inheritDoc}
     *
     * @param toRegister the registration request
     */
    @Override
    public void register(RegistrationRequestDTO toRegister) {
        super.register(toRegister, adminRegistrationStrategy);
    }

    /**
     * {@inheritDoc}
     *
     * @param adminId the id of the admin to disable
     */
    @Override
    public void disable(Integer adminId) {
        // At least one admin should be active
        if (this.adminUserRepository.findAll().size() < 2) {
            throw new UserNotDeletableException(AT_LEAST_ONE_ADMIN_CONFLICT);
        }
        this.adminUserRepository.softDeleteById(adminId);
    }

}
