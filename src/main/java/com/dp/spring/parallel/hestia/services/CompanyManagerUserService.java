package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.CompanyManagerUserRepository;
import com.dp.spring.parallel.hestia.services.registration_strategies.CompanyManagerRegistrationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Generic {@link User} services.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CompanyManagerUserService extends UserService {
    private final CompanyManagerRegistrationStrategy companyManagerRegistrationStrategy;
    private final CompanyManagerUserRepository companyManagerUserRepository;


    public void register(
            final Integer companyId,
            final RegistrationRequestDTO toRegister
    ) {
        checkCompanyExistenceOrThrow(companyId);
        checkPrincipalScopeOrThrow(companyId);
        super.register(companyId, toRegister, companyManagerRegistrationStrategy);
    }

    public Set<CompanyManagerUser> companyManagersFor(final Company company) {
        return this.companyManagerUserRepository.findAllByCompany(company);
    }


    public void disable(
            final Integer companyId,
            final Integer userId
    ) {
        checkCompanyExistenceOrThrow(companyId);
        checkPrincipalScopeOrThrow(companyId);
        this.companyManagerUserRepository.softDeleteById(userId);
    }
}
