package com.dp.spring.parallel.hephaestus.services.impl;

import com.dp.spring.parallel.common.exceptions.WorkplaceNameAlreadyExistsInWorkspaceException;
import com.dp.spring.parallel.common.exceptions.WorkplaceNotFoundException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.repositories.WorkplaceRepository;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import com.dp.spring.parallel.hephaestus.services.WorkspaceService;
import com.dp.spring.parallel.mnemosyne.services.WorkplaceBookingService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Workspace operations service implementation.
 */
@Service
@Transactional
public class WorkplaceServiceImpl extends BusinessService implements WorkplaceService {
    private final WorkspaceService workspaceService;
    private final WorkplaceBookingService workplaceBookingService;

    private final WorkplaceRepository workplaceRepository;


    public WorkplaceServiceImpl(
            WorkspaceService workspaceService,
            @Lazy WorkplaceBookingService workplaceBookingService,
            WorkplaceRepository workplaceRepository
    ) {
        this.workspaceService = workspaceService;
        this.workplaceBookingService = workplaceBookingService;
        this.workplaceRepository = workplaceRepository;
    }


    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace to link the workplace to
     * @param createRequest  the workplace creation request
     * @return the created workplace
     */
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

    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace
     * @param workplaceId    the id of the workplace
     * @return the workplace
     */
    @Override
    public Workplace workplace(Integer headquartersId, Integer workspaceId, Integer workplaceId) {
        // All permission checks are included
        final Workspace workspace = this.workspaceService.workspace(headquartersId, workspaceId);
        return this.workplaceRepository.findByIdAndWorkspace(workplaceId, workspace)
                .orElseThrow(() -> new WorkplaceNotFoundException(workplaceId, workspaceId));
    }

    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace
     * @return the workplaces
     */
    @Override
    public List<Workplace> workplaces(Integer headquartersId, Integer workspaceId) {
        // All permission checks are included
        final Workspace workspace = this.workspaceService.workspace(headquartersId, workspaceId);
        return this.workplaceRepository.findAllByWorkspace(workspace, Sort.by(Sort.Direction.ASC, "name"));
    }

    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace
     * @param workplaceId    the id of the workplace to update
     * @param updateRequest  the workspace update request
     * @return the updated workplace
     */
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

    /**
     * {@inheritDoc}
     *
     * @param headquartersId the id of the headquarters of the workspace
     * @param workspaceId    the id of the workspace
     * @param workplaceId    the id of the workplace to delete
     */
    @Override
    public void remove(Integer headquartersId, Integer workspaceId, Integer workplaceId) {
        // All permission checks are included
        final Workspace workspace = this.workspaceService.workspace(headquartersId, workspaceId);
        this.workplaceRepository.findByIdAndWorkspace(workplaceId, workspace)
                .ifPresent(this::softDelete);
    }

    /**
     * {@inheritDoc}
     *
     * @param workspace the workspace of which delete the workplaces
     */
    @Override
    public void removeAll(Workspace workspace) {
        // Get all the workplaces for the workspace, and soft delete each of them
        this.workplaceRepository.findAllByWorkspace(workspace)
                .forEach(this::softDelete);
    }


    /**
     * {@inheritDoc}
     *
     * @param headquarters the headquarters to count the workplaces of
     * @return the number of workplaces
     */
    @Override
    public long countForHeadquarters(Headquarters headquarters) {
        return this.workplaceRepository.countByWorkspaceHeadquarters(headquarters);
    }

    /**
     * {@inheritDoc}
     *
     * @param headquarters the headquarters to count available and total workplaces of
     * @param date         the date to check availability on
     * @return the number of available workplaces and the number of total workplaces
     */
    @Override
    public Pair<Long, Long> countAvailableOnTotalForHeadquarters(Headquarters headquarters, LocalDate date) {
        final long bookedWorkplaces = this.workplaceRepository.countNotAvailableByWorkspaceHeadquartersAndBookingDate(headquarters, date);
        final long totalWorkplaces = this.workplaceRepository.countByWorkspaceHeadquarters(headquarters);
        return Pair.of(totalWorkplaces - bookedWorkplaces, totalWorkplaces);
    }

    /**
     * {@inheritDoc}
     *
     * @param workspace the workspace to count available and total workplaces of
     * @param date      the date to check availability on
     * @return the number of available workplaces and the number of total workplaces
     */
    @Override
    public Pair<Long, Long> countAvailableOnTotal(Workspace workspace, LocalDate date) {
        final long bookedWorkplaces = this.workplaceRepository.countNotAvailableByWorkspaceAndBookingDate(workspace, date);
        final long totalWorkplaces = this.workplaceRepository.countByWorkspace(workspace);
        return Pair.of(totalWorkplaces - bookedWorkplaces, totalWorkplaces);
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
     * Internal method to soft delete a workplace.<br>
     * Before removing the workplace, removing all the related bookings.
     *
     * @param toDelete the workplace to be deleted
     */
    private void softDelete(final Workplace toDelete) {
        this.workplaceBookingService.cancelAll(toDelete);
        this.workplaceRepository.softDelete(toDelete);
    }

}
