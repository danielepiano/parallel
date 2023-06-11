package com.dp.spring.parallel.hephaestus.api.controllers.impl;

import com.dp.spring.parallel.hephaestus.api.controllers.WorkspaceController;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.WorkspaceResponseDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import com.dp.spring.parallel.hephaestus.services.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
public class WorkspaceControllerImpl implements WorkspaceController {
    private final WorkspaceService workspaceService;
    private final WorkplaceService workplaceService;


    @Override
    public WorkspaceResponseDTO addWorkspace(
            Integer headquartersId,
            CreateWorkspaceRequestDTO createRequest
    ) {
        final Workspace workspace = this.workspaceService.add(headquartersId, createRequest);
        return WorkspaceResponseDTO.of(workspace);
    }

    @Override
    public WorkspaceResponseDTO workspace(
            Integer headquartersId,
            Integer workspaceId
    ) {
        final Workspace workspace = this.workspaceService.workspace(headquartersId, workspaceId);
        return WorkspaceResponseDTO.of(workspace);
    }

    @Override
    public List<WorkspaceResponseDTO> workspaces(Integer headquartersId, LocalDate bookingDate) {
        return this.workspaceService.workspaces(headquartersId).stream()
                .map(workspace -> {
                    Pair<Long, Long> availableOnTotalWorkplaces = this.workplaceService
                            .countAvailableOnTotal(workspace, bookingDate);
                    return WorkspaceResponseDTO.of(workspace, availableOnTotalWorkplaces);
                })
                .toList();
    }

    @Override
    public WorkspaceResponseDTO updateWorkspace(
            Integer headquartersId,
            Integer workspaceId,
            UpdateWorkspaceRequestDTO updateRequest
    ) {
        final Workspace workspace = this.workspaceService.update(headquartersId, workspaceId, updateRequest);
        return WorkspaceResponseDTO.of(workspace);
    }

    @Override
    public void removeWorkspace(
            Integer headquartersId,
            Integer workspaceId
    ) {
        this.workspaceService.remove(headquartersId, workspaceId);
    }
}
