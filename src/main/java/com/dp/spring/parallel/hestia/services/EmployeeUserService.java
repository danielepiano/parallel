package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;

import java.util.Set;

/**
 * {@link EmployeeUser} operations.
 */
public interface EmployeeUserService {

    /**
     * Registration of an employee.
     *
     * @param companyId  the company of the employee to register
     * @param toRegister the registration request
     */
    void register(
            final Integer companyId,
            final RegistrationRequestDTO toRegister
    );

    /**
     * Retrieval of all employees of a given company.
     *
     * @param company the company
     * @return the employees of the given company
     */
    Set<EmployeeUser> employeesFor(final Company company);

    /**
     * Deactivation of all employees of a given company.
     *
     * @param company the company of the employees to be deleted
     */
    void disableEmployeesFor(final Company company);


    /**
     * Deactivation of an employee.
     *
     * @param companyId  the company of the company manager to register
     * @param employeeId the id of the employee to disable
     */
    void disable(
            final Integer companyId,
            final Integer employeeId
    );

}
