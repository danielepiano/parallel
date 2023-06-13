package com.dp.spring.parallel.hestia.services.impl;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.database.repositories.EmployeeUserRepository;
import com.dp.spring.parallel.hestia.services.EmployeeUserService;
import com.dp.spring.parallel.hestia.services.registration_strategies.EmployeeRegistrationStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * {@link EmployeeUserService} implementation.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeUserServiceImpl extends UserServiceImpl implements EmployeeUserService {
    private final EmployeeRegistrationStrategy employeeRegistrationStrategy;
    private final EmployeeUserRepository employeeUserRepository;


    /**
     * {@inheritDoc}
     *
     * @param companyId  the company of the employee to register
     * @param toRegister the registration request
     */
    @Override
    public void register(
            final Integer companyId,
            final RegistrationRequestDTO toRegister
    ) {
        checkCompanyExistenceOrThrow(companyId);
        checkPrincipalScopeOrThrow(companyId);
        super.register(companyId, toRegister, employeeRegistrationStrategy);
    }

    /**
     * {@inheritDoc}
     *
     * @param company the company
     * @return the employees of the given company
     */
    @Override
    public Set<EmployeeUser> employeesFor(final Company company) {
        return this.employeeUserRepository.findAllByCompany(company);
    }

    /**
     * {@inheritDoc}
     *
     * @param company the company of the employee to delete
     */
    public void disableEmployeesFor(final Company company) {
        this.employeeUserRepository.deleteAll(this.employeesFor(company));
    }

    /**
     * {@inheritDoc}
     *
     * @param companyId the company of the company manager to register
     * @param userId    the id of the employee to disable
     */
    public void disable(
            final Integer companyId,
            final Integer userId
    ) {
        checkCompanyExistenceOrThrow(companyId);
        checkPrincipalScopeOrThrow(companyId);
        this.employeeUserRepository.softDeleteById(userId);
    }
}
