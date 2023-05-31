package com.dp.spring.parallel.hephaestus.api.dtos;

import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.enums.WorkplaceType;
import com.dp.spring.parallel.hephaestus.database.enums.WorkspaceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkplaceResponseDTO {
    Integer id;
    String name;
    String description;
    WorkplaceType type;

    WorkspaceResponseDTO workspace;

    public static WorkplaceResponseDTO of(final Workplace workplace) {
        return WorkplaceResponseDTO.builder()
                .id(workplace.getId())
                .name(workplace.getName())
                .description(workplace.getDescription())
                .type(workplace.getType())
                .workspace(WorkspaceResponseDTO.of(workplace.getWorkspace()))
                .build();
    }

    @Builder
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WorkspaceResponseDTO {
        Integer id;
        String name;
        String description;
        WorkspaceType type;
        String floor;
        Integer maxSeats;

        public static WorkspaceResponseDTO of(final Workspace workspace) {
            return WorkspaceResponseDTO.builder()
                    .id(workspace.getId())
                    .name(workspace.getName())
                    .description(workspace.getDescription())
                    .type(workspace.getType())
                    .floor(workspace.getFloor())
                    .maxSeats(workspace.getMaxSeats())
                    .build();
        }
    }
}
