package com.dp.spring.parallel.hephaestus.api.controllers.impl;

import com.dp.spring.parallel.hephaestus.api.controllers.WorkplaceController;
import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.WorkplaceResponseDTO;
import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.services.WorkplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkplaceControllerImpl implements WorkplaceController {
    private final WorkplaceService workplaceService;

    @Override
    public WorkplaceResponseDTO addWorkplace(
            Integer headquartersId,
            Integer workspaceId,
            CreateWorkplaceRequestDTO createRequest
    ) {
        final Workplace workplace = this.workplaceService.add(headquartersId, workspaceId, createRequest);
        return WorkplaceResponseDTO.withWorkspace(workplace);
    }

    @Override
    public WorkplaceResponseDTO workplace(
            Integer headquartersId,
            Integer workspaceId,
            Integer workplaceId
    ) {
        final Workplace workplace = this.workplaceService.workplace(headquartersId, workspaceId, workplaceId);
        return WorkplaceResponseDTO.withWorkspace(workplace);
    }

    @Override
    public List<WorkplaceResponseDTO> workplaces(
            Integer headquartersId,
            Integer workspaceId
    ) {
        return this.workplaceService.workplaces(headquartersId, workspaceId).stream()
                .map(WorkplaceResponseDTO::of)
                .toList();
    }

    @Override
    public WorkplaceResponseDTO updateWorkplace(
            Integer headquartersId,
            Integer workspaceId,
            Integer workplaceId,
            UpdateWorkplaceRequestDTO updateRequest
    ) {
        final Workplace workplace = this.workplaceService.update(headquartersId, workspaceId, workplaceId, updateRequest);
        return WorkplaceResponseDTO.withWorkspace(workplace);
    }

    @Override
    public void removeWorkplace(
            Integer headquartersId,
            Integer workspaceId,
            Integer workplaceId
    ) {
        this.workplaceService.remove(headquartersId, workspaceId, workplaceId);
    }

}
