package com.dp.spring.parallel.hephaestus.services.impl;

import com.dp.spring.parallel.common.exceptions.CompanyAlreadyExistsException;
import com.dp.spring.parallel.common.exceptions.CompanyNotDeletableException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hephaestus.api.dtos.CompanyResponseDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.services.CompanyService;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.services.CompanyManagerUserService;
import com.dp.spring.parallel.hestia.services.EmployeeUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Company operations service implementation.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyServiceImpl extends BusinessService implements CompanyService {
    private final HeadquartersService headquartersService;
    private final CompanyManagerUserService companyManagerUserService;
    private final EmployeeUserService employeeUserService;


    /**
     * Adding a company.
     *
     * @param toAddData the data of the company to add
     */
    @Override
    public void add(CreateCompanyRequestDTO toAddData) {
        this.checkCompanyUniquenessOrThrow(toAddData.getName(), toAddData.getCity(), toAddData.getAddress());

        final Company toAdd = new Company()
                .setName(toAddData.getName())
                .setCity(toAddData.getCity())
                .setAddress(toAddData.getAddress())
                .setPhoneNumber(toAddData.getPhoneNumber())
                .setFeDescription(toAddData.getFeDescription())
                .setWebsiteUrl(toAddData.getWebsiteUrl());

        super.companyRepository.save(toAdd);
    }

    /**
     * Retrieving a company given its id.
     *
     * @param companyId the id of the headquarters to retrieve
     * @return the company
     */
    @Override
    public CompanyResponseDTO company(Integer companyId) {
        return CompanyResponseDTO.of(super.getCompanyOrThrow(companyId));
    }

    /**
     * Retrieving any company.
     *
     * @return the companies
     */
    @Override
    public Set<CompanyResponseDTO> companies() {
        return super.companyRepository.findAll().stream()
                .map(CompanyResponseDTO::of)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    /**
     * Updating a company.
     *
     * @param companyId   the id of the company to update
     * @param updatedData the new data of the company to update
     */
    @Override
    public void update(Integer companyId, UpdateCompanyRequestDTO updatedData) {
        final Company toUpdate = super.getCompanyOrThrow(companyId);
        super.checkPrincipalScopeOrThrow(companyId);

        this.checkCompanyUniquenessOrThrow(companyId, updatedData.getName(), updatedData.getCity(), updatedData.getAddress());

        toUpdate.setName(updatedData.getName());
        toUpdate.setCity(updatedData.getCity());
        toUpdate.setAddress(updatedData.getAddress());
        toUpdate.setPhoneNumber(updatedData.getPhoneNumber());
        toUpdate.setFeDescription(updatedData.getFeDescription());
        toUpdate.setWebsiteUrl(updatedData.getWebsiteUrl());

        super.companyRepository.save(toUpdate);
    }

    /**
     * Removing a company.<br>
     * If any {@link CompanyManagerUser} still exists for the company, it will not be deleted.<br>
     * Before removing the company, deleting all related headquarters and employees.
     *
     * @param companyId the id of the company to delete
     */
    @Override
    public void remove(Integer companyId) {
        final Company toDelete = super.getCompanyOrThrow(companyId);
        super.checkPrincipalScopeOrThrow(companyId);

        // The set of Company Managers must be empty
        if (!this.companyManagerUserService.companyManagersFor(toDelete).isEmpty()) {
            throw new CompanyNotDeletableException(companyId);
        }

        this.headquartersService.removeAll(toDelete);
        this.employeeUserService.disableEmployeesFor(toDelete);

        super.companyRepository.softDelete(toDelete);
    }


    /**
     * On creation, checking the company uniqueness amongst all companies.
     *
     * @param name    the name of the company
     * @param city    the city of the company
     * @param address the address of the company
     */
    private void checkCompanyUniquenessOrThrow(final String name, final String city, final String address) {
        if (super.companyRepository.existsByNameAndCityAndAddress(name, city, address)) {
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
        if (super.companyRepository.existsByIdNotAndNameAndCityAndAddress(companyId, name, city, address)) {
            throw new CompanyAlreadyExistsException(name, city, address);
        }
    }
}
