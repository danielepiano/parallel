package com.dp.spring.parallel.hestia.services.impl;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.repositories.CompanyManagerUserRepository;
import com.dp.spring.parallel.hestia.services.CompanyManagerUserService;
import com.dp.spring.parallel.hestia.services.registration_strategies.CompanyManagerRegistrationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * {@link CompanyManagerUserService} implementation.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CompanyManagerUserServiceImpl extends UserServiceImpl implements CompanyManagerUserService {
    private final CompanyManagerRegistrationStrategy companyManagerRegistrationStrategy;
    private final CompanyManagerUserRepository companyManagerUserRepository;


    /**
     * {@inheritDoc}
     *
     * @param companyId  the company of the company manager to register
     * @param toRegister the registration request
     */
    @Override
    public void register(
            Integer companyId,
            RegistrationRequestDTO toRegister
    ) {
        checkCompanyExistenceOrThrow(companyId);
        checkPrincipalScopeOrThrow(companyId);
        super.register(companyId, toRegister, companyManagerRegistrationStrategy);
    }

    /**
     * {@inheritDoc}
     *
     * @param company the company
     * @return the company managers of the given company
     */
    @Override
    public Set<CompanyManagerUser> companyManagersFor(Company company) {
        return this.companyManagerUserRepository.findAllByCompany(company);
    }

    /**
     * {@inheritDoc}
     *
     * @param companyId        the company of the company manager to register
     * @param companyManagerId the id of the company manager to disable
     */
    @Override
    public void disable(
            Integer companyId,
            Integer companyManagerId
    ) {
        checkCompanyExistenceOrThrow(companyId);
        checkPrincipalScopeOrThrow(companyId);
        this.companyManagerUserRepository.softDeleteById(companyManagerId);
    }
}
