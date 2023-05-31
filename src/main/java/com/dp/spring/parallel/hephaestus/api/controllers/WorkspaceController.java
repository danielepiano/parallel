package com.dp.spring.parallel.hephaestus.api.controllers;

import com.dp.spring.parallel.hephaestus.api.dtos.WorkspaceResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/api/v1/headquarters/{headquartersId}/workspaces")
public interface WorkspaceController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    void addWorkspace(
            @PathVariable("headquartersId") Integer headquartersId
            // @todo createrequest
    );

    @GetMapping(path = "/{workspaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    WorkspaceResponseDTO workspace(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId

    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    Set<WorkspaceResponseDTO> workspaces(
            @PathVariable("headquartersId") Integer headquartersId
    );

    @PutMapping(path = "/{workspaceId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateWorkspace(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId
            // @todo updaterequest
    );

    @DeleteMapping(path = "/{workspaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeWorkspace(
            @PathVariable("headquartersId") Integer headquartersId,
            @PathVariable("workspaceId") Integer workspaceId
    );
}
