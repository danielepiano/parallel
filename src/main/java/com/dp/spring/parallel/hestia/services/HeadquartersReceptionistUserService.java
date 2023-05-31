package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;

import java.util.Set;

/**
 * {@link HeadquartersReceptionistUser} operations.
 */
public interface HeadquartersReceptionistUserService {

    /**
     * Registration of a headquarters receptionist.
     *
     * @param companyId      the company of the headquarters
     * @param headquartersId the headquarters of the headquarters receptionist to register
     * @param toRegister     the registration request
     */
    void register(
            final Integer companyId,
            final Integer headquartersId,
            final RegistrationRequestDTO toRegister
    );

    /**
     * Retrieval of all headquarters receptionist of a given company.
     *
     * @param headquarters the headquarters
     * @return the headquarters receptionists of the given headquarters
     */
    Set<HeadquartersReceptionistUser> headquartersReceptionistsFor(final Headquarters headquarters);

    /**
     * Deactivation of a headquarters receptionist.
     *
     * @param companyId        the company of the headquarters
     * @param headquartersId   the headquarters of the headquarters receptionist to register
     * @param companyManagerId the id of the headquarters receptionist to disable
     */
    void disable(
            final Integer companyId,
            final Integer headquartersId,
            final Integer companyManagerId
    );

}
