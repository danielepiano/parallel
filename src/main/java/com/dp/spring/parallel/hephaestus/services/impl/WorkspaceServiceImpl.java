package com.dp.spring.parallel.hephaestus.services.impl;

import com.dp.spring.parallel.common.exceptions.WorkspaceNameAlreadyExistsInHeadquartersException;
import com.dp.spring.parallel.common.exceptions.WorkspaceNotFoundException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkspaceRepository;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import com.dp.spring.parallel.hephaestus.services.WorkspaceService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Workspace operations service implementation.
 */
@Service
@Transactional
public class WorkspaceServiceImpl extends BusinessService implements WorkspaceService {
    private final WorkplaceService workplaceService;

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceServiceImpl(@Lazy WorkplaceService workplaceService, WorkspaceRepository workspaceRepository) {
        this.workplaceService = workplaceService;
        this.workspaceRepository = workspaceRepository;
    }


    @Override
    public Workspace add(Integer headquartersId, CreateWorkspaceRequestDTO createRequest) {
        final Headquarters headquarters = this.getAndCheckHeadquartersOrThrow(headquartersId);
        this.checkWorkspaceNameUniquenessOrThrow(createRequest.getName(), headquarters);

        final Workspace toAdd = new Workspace();
        toAdd.setHeadquarters(headquarters)
                .setName(createRequest.getName())
                .setDescription(createRequest.getDescription())
                .setType(createRequest.getType())
                .setFloor(createRequest.getFloor())
                .setMaxSeats(createRequest.getMaxSeats());

        return this.workspaceRepository.save(toAdd);
    }

    @Override
    public Workspace workspace(Integer headquartersId, Integer workspaceId) {
        final Headquarters headquarters = this.getAndCheckHeadquartersOrThrow(headquartersId);
        return this.workspaceRepository.findByIdAndHeadquarters(workspaceId, headquarters)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId, headquartersId));
    }

    @Override
    public Set<Workspace> workspaces(Integer headquartersId) {
        final Headquarters headquarters = this.getAndCheckHeadquartersOrThrow(headquartersId);
        return this.workspaceRepository.findAllByHeadquarters(headquarters);
    }

    @Override
    public Workspace update(Integer headquartersId, Integer workspaceId, UpdateWorkspaceRequestDTO updateRequest) {
        final Headquarters headquarters = this.getAndCheckHeadquartersOrThrow(headquartersId);
        this.checkWorkspaceNameUniquenessOrThrow(workspaceId, updateRequest.getName(), headquarters);

        final Workspace toUpdate = this.workspaceRepository.findByIdAndHeadquarters(workspaceId, headquarters)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId, headquartersId))
                .setName(updateRequest.getName())
                .setDescription(updateRequest.getDescription())
                .setType(updateRequest.getType())
                .setFloor(updateRequest.getFloor())
                .setMaxSeats(updateRequest.getMaxSeats());

        return this.workspaceRepository.save(toUpdate);
    }

    @Override
    public void remove(Integer headquartersId, Integer workspaceId) {
        final Headquarters headquarters = this.getAndCheckHeadquartersOrThrow(headquartersId);
        this.workspaceRepository.findByIdAndHeadquarters(workspaceId, headquarters)
                .ifPresent(this::softDelete);
    }

    @Override
    public void removeAll(Headquarters headquarters) {
        // Get all the workspaces for the headquarters, and soft delete each of them
        this.workspaceRepository.findAllByHeadquarters(headquarters)
                .forEach(this::softDelete);
    }

    /**
     * Checking if the give headquarters is under the principal control. In case, returning the headquarters.
     *
     * @param headquartersId the id of the headquarters to get and check
     * @return the headquarters
     */
    Headquarters getAndCheckHeadquartersOrThrow(final Integer headquartersId) {
        final Headquarters headquarters = super.getHeadquartersOrThrow(headquartersId);
        super.checkPrincipalScopeOrThrow(headquarters.getCompany().getId());
        return headquarters;
    }


    /**
     * On creation, checking the workspace name uniqueness amongst headquarters workspaces.
     *
     * @param name         the name of the workspace to check
     * @param headquarters the headquarters to check the workspaces of
     */
    private void checkWorkspaceNameUniquenessOrThrow(final String name, final Headquarters headquarters) {
        if (this.workspaceRepository.existsByNameAndHeadquarters(name, headquarters)) {
            throw new WorkspaceNameAlreadyExistsInHeadquartersException(name, headquarters.getId());
        }
    }

    /**
     * On update, checking the workspace name uniqueness amongst headquarters workspaces.
     *
     * @param workspaceId  the id of the workspace to ignore
     * @param name         the name of the workspace to check
     * @param headquarters the headquarters to check the workspaces of
     */
    private void checkWorkspaceNameUniquenessOrThrow(final Integer workspaceId, final String name, final Headquarters headquarters) {
        // In case of update, the workspace to update itself should be ignored, when searching for uniqueness constraint !
        if (this.workspaceRepository.existsByIdNotAndNameAndHeadquarters(workspaceId, name, headquarters)) {
            throw new WorkspaceNameAlreadyExistsInHeadquartersException(name, headquarters.getId());
        }
    }


    /**
     * Internal method to soft delete a workspace.<br>
     * Before removing the workspace, removing all the related workplaces.
     *
     * @param toDelete the workspace to be deleted
     */
    private void softDelete(final Workspace toDelete) {
        this.workplaceService.removeAll(toDelete);
        this.workspaceRepository.softDelete(toDelete);
    }

}