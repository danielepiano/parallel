package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.EmployeeUserRepository;
import com.dp.spring.parallel.hestia.services.registration_strategies.EmployeeRegistrationStrategy;
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
public class EmployeeUserService extends UserService {
    private final EmployeeRegistrationStrategy employeeRegistrationStrategy;
    private final EmployeeUserRepository employeeUserRepository;


    public void register(
            final Integer companyId,
            final RegistrationRequestDTO toRegister
    ) {
        checkCompanyExistenceOrThrow(companyId);
        checkPrincipalScopeOrThrow(companyId);
        super.register(companyId, toRegister, employeeRegistrationStrategy);
    }

    public Set<EmployeeUser> employeesFor(final Company company) {
        return this.employeeUserRepository.findAllByCompany(company);
    }

    /**
     * Removing any employees for the given company.
     *
     * @param company the company of the employee to delete
     */
    public void disableEmployeesFor(final Company company) {
        this.employeeUserRepository.deleteAll(this.employeesFor(company));
    }

    public void disable(
            final Integer companyId,
            final Integer userId
    ) {
        checkCompanyExistenceOrThrow(companyId);
        checkPrincipalScopeOrThrow(companyId);
        this.employeeUserRepository.softDeleteById(userId);
    }
}
