package com.dp.spring.parallel.hephaestus.services.impl;

import com.dp.spring.parallel.common.exceptions.WorkplaceNameAlreadyExistsInWorkspaceException;
import com.dp.spring.parallel.common.exceptions.WorkplaceNotFoundException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkplaceRepository;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import com.dp.spring.parallel.hephaestus.services.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Workspace operations service implementation.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class WorkplaceServiceImpl extends BusinessService implements WorkplaceService {
    private final WorkspaceService workspaceService;

    private final WorkplaceRepository workplaceRepository;


    @Override
    public Workplace add(Integer headquartersId, Integer workspaceId, CreateWorkplaceRequestDTO createRequest) {
        // All permission checks are included
        final Workspace workspace = this.workspaceService.workspace(headquartersId, workspaceId);
        this.checkWorkplaceNameUniquenessOrThrow(createRequest.getName(), workspace);

        final Workplace toAdd = new Workplace();
        toAdd.setWorkspace(workspace)
                .setName(createRequest.getName())
                .setDescription(createRequest.getDescription())
                .setType(createRequest.getType());

        return this.workplaceRepository.save(toAdd);
    }

    @Override
    public Workplace workplace(Integer headquartersId, Integer workspaceId, Integer workplaceId) {
        // All permission checks are included
        final Workspace workspace = this.workspaceService.workspace(headquartersId, workspaceId);
        return this.workplaceRepository.findByIdAndWorkspace(workplaceId, workspace)
                .orElseThrow(() -> new WorkplaceNotFoundException(workplaceId, workspaceId));
    }

    @Override
    public Set<Workplace> workplaces(Integer headquartersId, Integer workspaceId) {
        // All permission checks are included
        final Workspace workspace = this.workspaceService.workspace(headquartersId, workspaceId);
        return this.workplaceRepository.findAllByWorkspace(workspace);
    }

    @Override
    public Workplace update(Integer headquartersId, Integer workspaceId, Integer workplaceId, UpdateWorkplaceRequestDTO updateRequest) {
        // All permission checks are included
        final Workspace workspace = this.workspaceService.workspace(headquartersId, workspaceId);
        this.checkWorkplaceNameUniquenessOrThrow(workplaceId, updateRequest.getName(), workspace);

        final Workplace toUpdate = this.workplaceRepository.findByIdAndWorkspace(workplaceId, workspace)
                .orElseThrow(() -> new WorkplaceNotFoundException(workplaceId, workspaceId))
                .setName(updateRequest.getName())
                .setDescription(updateRequest.getDescription())
                .setType(updateRequest.getType());

        return this.workplaceRepository.save(toUpdate);
    }

    @Override
    public void remove(Integer headquartersId, Integer workspaceId, Integer workplaceId) {
        // All permission checks are included
        final Workspace workspace = this.workspaceService.workspace(headquartersId, workspaceId);
        this.workplaceRepository.findByIdAndWorkspace(workplaceId, workspace)
                .ifPresent(this::softDelete);
    }

    @Override
    public void removeAll(Workspace workspace) {
        // Get all the workplaces for the workspace, and soft delete each of them
        this.workplaceRepository.findAllByWorkspace(workspace)
                .forEach(this::softDelete);
    }


    /**
     * On creation, checking the workplace name uniqueness amongst workspace workplaces.
     *
     * @param name      the name of the workplace to check
     * @param workspace the workspace to check the workplaces of
     */
    private void checkWorkplaceNameUniquenessOrThrow(final String name, final Workspace workspace) {
        if (this.workplaceRepository.existsByNameAndWorkspace(name, workspace)) {
            throw new WorkplaceNameAlreadyExistsInWorkspaceException(name, workspace.getId());
        }
    }

    /**
     * On update, checking the workplace name uniqueness amongst workspace workplaces.
     *
     * @param workplaceId the id of the workplace to ignore, in case of update
     * @param name        the name of the workplace to check
     * @param workspace   the workspace to check the workplaces of
     */
    private void checkWorkplaceNameUniquenessOrThrow(final Integer workplaceId, final String name, final Workspace workspace) {
        // In case of update, the workplace to update itself should be ignored, when searching for uniqueness constraint !
        if (this.workplaceRepository.existsByIdNotAndNameAndWorkspace(workplaceId, name, workspace)) {
            throw new WorkplaceNameAlreadyExistsInWorkspaceException(name, workspace.getId());
        }
    }


    /**
     * Internal method to soft delete a workspace.<br>
     * Before removing the workplace, removing all the related future bookings.
     *
     * @param toDelete the workplace to be deleted
     */
    private void softDelete(final Workplace toDelete) {
        // @todo soft delete booking - removeAllFuture(workplace)

        this.workplaceRepository.softDelete(toDelete);
    }

}
