package com.dp.spring.parallel.hephaestus.services;

import com.dp.spring.parallel.hephaestus.api.dtos.CreateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateCompanyRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;

import java.util.Set;

public interface CompanyService {

    /**
     * Adding a company.
     *
     * @param toAddData the data of the company to add
     * @return the created company
     */
    Company add(CreateCompanyRequestDTO toAddData);

    /**
     * Retrieving a company given its id.
     *
     * @param companyId the id of the headquarters to retrieve
     * @return the company
     */
    Company company(Integer companyId);

    /**
     * Retrieving any company.
     *
     * @return the companies
     */
    Set<Company> companies();

    /**
     * Updating a company.
     *
     * @param companyId   the id of the company to update
     * @param updatedData the new data of the company to update
     * @return the updated company
     */
    Company update(Integer companyId, UpdateCompanyRequestDTO updatedData);


    /**
     * Removing a company.
     *
     * @param companyId the id of the company to delete
     */
    void remove(Integer companyId);
}
