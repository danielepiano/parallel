package com.dp.spring.parallel.hephaestus.services;

import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.util.List;

public interface WorkplaceService {

    /**
     * Adding a workplace.
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace to link the workplace to
     * @param createRequest  the workplace creation request
     * @return the created workplace
     */
    Workplace add(
            Integer headquartersId,
            Integer workspaceId,
            CreateWorkplaceRequestDTO createRequest
    );

    /**
     * Retrieving a workplace given its id and the ids of its headquarters and workspace.
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace
     * @param workplaceId    the id of the workplace
     * @return the workplace
     */
    Workplace workplace(
            Integer headquartersId,
            Integer workspaceId,
            Integer workplaceId
    );

    /**
     * Retrieving all the workplaces of a given headquarters' workspace.
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace
     * @return the workplaces
     */
    List<Workplace> workplaces(
            Integer headquartersId,
            Integer workspaceId
    );

    /**
     * Retrieving all the available workplaces of a given headquarters' workspace on a given date.
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace
     * @param onDate         the date
     * @return the available workplaces
     */
    List<Workplace> availableWorkplaces(
            Integer headquartersId,
            Integer workspaceId,
            LocalDate onDate
    );

    /**
     * Updating a workplace.
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace
     * @param workplaceId    the id of the workplace to update
     * @param updateRequest  the workspace update request
     * @return the updated workplace
     */
    Workplace update(
            Integer headquartersId,
            Integer workspaceId,
            Integer workplaceId,
            UpdateWorkplaceRequestDTO updateRequest
    );

    /**
     * Removing a workplace.
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace
     * @param workplaceId    the id of the workplace to delete
     */
    void remove(
            Integer headquartersId,
            Integer workspaceId,
            Integer workplaceId
    );

    /**
     * Removing all the workplaces of the given workspace.
     *
     * @param workspace the workspace of which delete the workplaces
     */
    void removeAll(
            Workspace workspace
    );


    /**
     * Counting the number of workplaces for a given headquarters.
     *
     * @param headquarters the headquarters to count the workplaces of
     * @return the number of workplaces
     */
    long countForHeadquarters(Headquarters headquarters);


    /**
     * Counting available (on a given date) and total workplaces for a given headquarters.
     *
     * @param headquarters the headquarters to count available and total workplaces of
     * @param date         the date to check availability on
     * @return the number of available workplaces and the number of total workplaces
     */
    Pair<Long, Long> countAvailableOnTotalForHeadquarters(Headquarters headquarters, LocalDate date);


    /**
     * Counting available (on a given date) and total workplaces for a given workspace.
     *
     * @param workspace the workspace to count available and total workplaces of
     * @param date      the date to check availability on
     * @return the number of available workplaces and the number of total workplaces
     */
    Pair<Long, Long> countAvailableOnTotal(Workspace workspace, LocalDate date);

}
