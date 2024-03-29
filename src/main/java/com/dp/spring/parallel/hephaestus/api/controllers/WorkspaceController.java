package com.dp.spring.parallel.hephaestus.api.controllers;

import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkspaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.WorkspaceResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.ROLE_ADMIN_VALUE;
import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.ROLE_COMPANY_MANAGER_VALUE;

@RequestMapping("/api/v1/headquarters/{headquartersId}/workspaces")
public interface WorkspaceController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    WorkspaceResponseDTO addWorkspace(
            @PathVariable("headquartersId") Integer headquartersId,
            @Valid @RequestBody CreateWorkspaceRequestDTO createRequest
    );

    @GetMapping(path = "/{workspaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    WorkspaceResponseDTO workspace(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId
    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<WorkspaceResponseDTO> workspaces(
            @PathVariable("headquartersId") Integer headquartersId,
            @RequestParam("bookingDate") LocalDate bookingDate
    );

    @PutMapping(path = "/{workspaceId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    WorkspaceResponseDTO updateWorkspace(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId,
            @Valid @RequestBody UpdateWorkspaceRequestDTO updateRequest
    );

    @DeleteMapping(path = "/{workspaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeWorkspace(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId
    );

}
