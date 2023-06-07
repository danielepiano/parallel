package com.dp.spring.parallel.hephaestus.services;

import com.dp.spring.parallel.hephaestus.api.dtos.CreateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateHeadquartersRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;

import java.util.Set;

public interface HeadquartersService {

    /**
     * Adding a headquarters.
     *
     * @param companyId the id of the company of the headquarters to add
     * @param toAddData the data of the headquarters to add
     * @return the created headquarters
     */
    Headquarters add(Integer companyId, CreateHeadquartersRequestDTO toAddData);

    /**
     * Retrieving a headquarters given its id.
     *
     * @param headquartersId the id of the headquarters to retrieve
     * @return the headquarters
     */
    Headquarters headquarters(Integer headquartersId);

    /**
     * Retrieving any headquarters of any company.
     *
     * @return the headquarters
     */
    Set<Headquarters> headquarters();

    /**
     * Retrieving the headquarters of a given company.
     *
     * @param companyId the id of the company
     * @return the headquarters of the company
     */
    Set<Headquarters> companyHeadquarters(Integer companyId);

    /**
     * Updating a headquarters.
     *
     * @param companyId      the id of the company of the headquarters to update
     * @param headquartersId the id of the headquarters to update
     * @param updatedData    the new data of the headquarters to update
     * @return the updated headquarters
     */
    Headquarters update(Integer companyId, Integer headquartersId, UpdateHeadquartersRequestDTO updatedData);

    /**
     * Removing a headquarters.
     *
     * @param companyId      the id of the company of the headquarters to delete
     * @param headquartersId the id of the headquarters to delete
     */
    void remove(Integer companyId, Integer headquartersId);

    /**
     * Removing any headquarters for a given company.
     *
     * @param company the company to delete the headquarters of
     */
    void removeAll(Company company);

}
