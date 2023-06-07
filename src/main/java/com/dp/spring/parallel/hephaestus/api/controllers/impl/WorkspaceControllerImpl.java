package com.dp.spring.parallel.hephaestus.api.controllers.impl;

import com.dp.spring.parallel.hephaestus.api.controllers.WorkspaceController;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.WorkspaceResponseDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.services.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController
@RequiredArgsConstructor
@Transactional
public class WorkspaceControllerImpl implements WorkspaceController {
    private final WorkspaceService workspaceService;


    @Override
    public WorkspaceResponseDTO addWorkspace(
            Integer headquartersId,
            CreateWorkspaceRequestDTO createRequest
    ) {
        final Workspace workspace = this.workspaceService.add(headquartersId, createRequest);
        return WorkspaceResponseDTO.withWorkplaces(workspace);
    }

    @Override
    public WorkspaceResponseDTO workspace(
            Integer headquartersId,
            Integer workspaceId
    ) {
        final Workspace workspace = this.workspaceService.workspace(headquartersId, workspaceId);
        return WorkspaceResponseDTO.withWorkplaces(workspace);
    }

    @Override
    public Set<WorkspaceResponseDTO> workspaces(Integer headquartersId) {
        return this.workspaceService.workspaces(headquartersId).stream()
                .map(WorkspaceResponseDTO::of)
                .collect(toSet());
    }

    @Override
    public WorkspaceResponseDTO updateWorkspace(
            Integer headquartersId,
            Integer workspaceId,
            UpdateWorkspaceRequestDTO updateRequest
    ) {
        final Workspace workspace = this.workspaceService.update(headquartersId, workspaceId, updateRequest);
        return WorkspaceResponseDTO.withWorkplaces(workspace);
    }

    @Override
    public void removeWorkspace(
            Integer headquartersId,
            Integer workspaceId
    ) {
        this.workspaceService.remove(headquartersId, workspaceId);
    }
}
