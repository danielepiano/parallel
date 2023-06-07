package com.dp.spring.parallel.hephaestus.services;

import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;

import java.util.Set;

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
     * Retrieving all the workplaces of the headquarters and workspace of given ids;
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace
     * @return the workplaces
     */
    Set<Workplace> workplaces(
            Integer headquartersId,
            Integer workspaceId
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

}
