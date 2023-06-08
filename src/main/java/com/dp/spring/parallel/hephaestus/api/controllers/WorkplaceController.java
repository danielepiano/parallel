package com.dp.spring.parallel.hephaestus.api.controllers;

import com.dp.spring.parallel.hephaestus.api.dtos.CreateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.UpdateWorkplaceRequestDTO;
import com.dp.spring.parallel.hephaestus.api.dtos.WorkplaceResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.ROLE_ADMIN_VALUE;
import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.ROLE_COMPANY_MANAGER_VALUE;

@RequestMapping("/api/v1/headquarters/{headquartersId}/workspaces/{workspaceId}/workplaces")
public interface WorkplaceController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    WorkplaceResponseDTO addWorkplace(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId,
            @RequestBody CreateWorkplaceRequestDTO createRequest
    );

    @GetMapping(path = "/{workplaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    WorkplaceResponseDTO workplace(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId,
            @PathVariable("workplaceId") Integer workplaceId

    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<WorkplaceResponseDTO> workplaces(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId
    );

    @PutMapping(path = "/{workplaceId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    WorkplaceResponseDTO updateWorkplace(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId,
            @PathVariable("workplaceId") Integer workplaceId,
            @RequestBody UpdateWorkplaceRequestDTO updateRequest
    );

    @DeleteMapping(path = "/{workplaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeWorkplace(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId,
            @PathVariable("workplaceId") Integer workplaceId
    );

}
