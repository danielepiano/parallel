package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;

import java.util.Set;

/**
 * {@link CompanyManagerUser} operations.
 */
public interface CompanyManagerUserService {

    /**
     * Registration of a company manager.
     *
     * @param companyId  the company of the company manager to register
     * @param toRegister the registration request
     */
    void register(
            final Integer companyId,
            final RegistrationRequestDTO toRegister
    );

    /**
     * Retrieval of all company managers of a given company.
     *
     * @param company the company
     * @return the company managers of the given company
     */
    Set<CompanyManagerUser> companyManagersFor(final Company company);

    /**
     * Deactivation of a company manager.
     *
     * @param companyId        the company of the company manager to register
     * @param companyManagerId the id of the company manager to disable
     */
    void disable(
            final Integer companyId,
            final Integer companyManagerId
    );

}
