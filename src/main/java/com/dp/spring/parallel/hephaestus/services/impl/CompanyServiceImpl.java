package com.dp.spring.parallel.hephaestus.services.impl;

import com.dp.spring.parallel.common.exceptions.CompanyAlreadyExistsException;
import com.dp.spring.parallel.common.exceptions.CompanyNotDeletableException;
import com.dp.spring.parallel.common.exceptions.CompanyNotFoundException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.repositories.CompanyRepository;
import com.dp.spring.parallel.hephaestus.services.CompanyService;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.services.CompanyManagerUserService;
import com.dp.spring.parallel.hestia.services.EmployeeUserService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Company operations service implementation.
 */
@Service
@Transactional
public class CompanyServiceImpl extends BusinessService implements CompanyService {
    private final HeadquartersService headquartersService;
    private final CompanyManagerUserService companyManagerUserService;
    private final EmployeeUserService employeeUserService;

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(
            HeadquartersService headquartersService,
            @Lazy CompanyManagerUserService companyManagerUserService,
            @Lazy EmployeeUserService employeeUserService,
            CompanyRepository companyRepository) {
        this.headquartersService = headquartersService;
        this.companyManagerUserService = companyManagerUserService;
        this.employeeUserService = employeeUserService;
        this.companyRepository = companyRepository;
    }


    /**
     * {@inheritDoc}
     *
     * @param toAddData the data of the company to add
     * @return the created company
     */
    @Override
    public Company add(CreateCompanyRequestDTO toAddData) {
        this.checkCompanyUniquenessOrThrow(toAddData.getName(), toAddData.getCity(), toAddData.getAddress());

        final Company toAdd = new Company()
                .setName(toAddData.getName())
                .setCity(toAddData.getCity())
                .setAddress(toAddData.getAddress())
                .setPhoneNumber(toAddData.getPhoneNumber())
                .setDescription(toAddData.getDescription())
                .setWebsiteUrl(toAddData.getWebsiteUrl());

        return companyRepository.save(toAdd);
    }

    /**
     * {@inheritDoc}
     *
     * @param companyId the id of the headquarters to retrieve
     * @return the company
     */
    @Override
    public Company company(Integer companyId) {
        return getCompanyOrThrow(companyId);
    }

    /**
     * {@inheritDoc}
     *
     * @return the companies
     */
    @Override
    public Set<Company> companies() {
        return Set.copyOf(companyRepository.findAll());
    }

    /**
     * {@inheritDoc}
     *
     * @param companyId   the id of the company to update
     * @param updatedData the new data of the company to update
     * @return the updated company
     */
    @Override
    public Company update(Integer companyId, UpdateCompanyRequestDTO updatedData) {
        final Company toUpdate = getCompanyOrThrow(companyId);
        super.checkPrincipalScopeOrThrow(companyId);

        this.checkCompanyUniquenessOrThrow(companyId, updatedData.getName(), updatedData.getCity(), updatedData.getAddress());

        toUpdate.setName(updatedData.getName());
        toUpdate.setCity(updatedData.getCity());
        toUpdate.setAddress(updatedData.getAddress());
        toUpdate.setPhoneNumber(updatedData.getPhoneNumber());
        toUpdate.setDescription(updatedData.getDescription());
        toUpdate.setWebsiteUrl(updatedData.getWebsiteUrl());

        return this.companyRepository.save(toUpdate);
    }

    /**
     * {@inheritDoc}<br>
     * If any {@link CompanyManagerUser} still exists for the company, it will not be deleted.<br>
     * Before removing the company, deleting all related headquarters and employees.
     *
     * @param companyId the id of the company to delete
     */
    @Override
    public void remove(Integer companyId) {
        final Company toDelete = this.getCompanyOrThrow(companyId);
        super.checkPrincipalScopeOrThrow(companyId);

        // The set of Company Managers must be empty
        if (!this.companyManagerUserService.companyManagersFor(toDelete).isEmpty()) {
            throw new CompanyNotDeletableException(companyId);
        }

        this.headquartersService.removeAll(toDelete);
        this.employeeUserService.disableEmployeesFor(toDelete);

        this.companyRepository.softDelete(toDelete);
    }

    /**
     * {@inheritDoc}
     *
     * @param companyId the id of the company
     */
    @Override
    public void checkExistence(Integer companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }
    }

    /**
     * Getting a company by only its id, otherwise returning a {@link CompanyNotFoundException}.
     *
     * @param companyId the id of the company
     * @return the company
     */
    private Company getCompanyOrThrow(final Integer companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));
    }

    /**
     * On creation, checking the company uniqueness amongst all companies.
     *
     * @param name    the name of the company
     * @param city    the city of the company
     * @param address the address of the company
     */
    private void checkCompanyUniquenessOrThrow(final String name, final String city, final String address) {
        if (companyRepository.existsByNameAndCityAndAddress(name, city, address)) {
            throw new CompanyAlreadyExistsException(name, city, address);
        }
    }

    /**
     * On update, checking the company uniqueness amongst all companies.
     *
     * @param companyId the id of the company to update
     * @param name      the name of the company
     * @param city      the city of the company
     * @param address   the address of the company
     */
    private void checkCompanyUniquenessOrThrow(
            final Integer companyId,
            final String name,
            final String city,
            final String address
    ) {
        // The company to update itself should be ignored, when searching for uniqueness constraint !
        if (this.companyRepository.existsByIdNotAndNameAndCityAndAddress(companyId, name, city, address)) {
            throw new CompanyAlreadyExistsException(name, city, address);
        }
    }
}
