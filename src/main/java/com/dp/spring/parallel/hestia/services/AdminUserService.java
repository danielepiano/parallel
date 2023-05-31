package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.AdminUser;

/**
 * {@link AdminUser} operations.
 */
public interface AdminUserService {

    /**
     * Registration of an admin.
     *
     * @param toRegister the registration request
     */
    void register(final RegistrationRequestDTO toRegister);

    /**
     * Deactivation of an admin.
     *
     * @param adminId the id of the admin to disable
     */
    void disable(final Integer adminId);

}
