package com.dp.spring.parallel.hephaestus.services;

import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;

import java.util.List;

public interface WorkspaceService {

    /**
     * Adding a workspace.
     *
     * @param headquartersId the id of the headquarters to link the workspace to
     * @param createRequest  the workspace creation request
     * @return the created workspace
     */
    Workspace add(
            Integer headquartersId,
            CreateWorkspaceRequestDTO createRequest
    );

    /**
     * Retrieving a workspace given its id and the id of its headquarters.
     *
     * @param headquartersId the id of the headquarters
     * @param workspaceId    the id of the workspace
     * @return the workspace
     */
    Workspace workspace(
            Integer headquartersId,
            Integer workspaceId
    );

    /**
     * Retrieving all the workspaces of the headquarters of given id.
     *
     * @param headquartersId the id of the headquarters
     * @return the workspaces
     */
    List<Workspace> workspaces(
            Integer headquartersId
    );

    /**
     * Updating a workspace.
     *
     * @param headquartersId the id of the headquarters
     * @param workspaceId    the id of the workspace to update
     * @param updateRequest  the workspace update request
     * @return the updated workspace
     */
    Workspace update(
            Integer headquartersId,
            Integer workspaceId,
            UpdateWorkspaceRequestDTO updateRequest
    );

    /**
     * Removing a workspace.
     *
     * @param headquartersId the id of the headquarters
     * @param workspaceId    the id of the workspace to delete
     */
    void remove(
            Integer headquartersId,
            Integer workspaceId
    );

    /**
     * Removing all the workspaces of the given headquarters.
     *
     * @param headquarters the headquarters of which delete the workspaces
     */
    void removeAll(
            Headquarters headquarters
    );

}
