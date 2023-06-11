package com.dp.spring.parallel.hephaestus.api.dtos;

import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.enums.WorkspaceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.util.Pair;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkspaceResponseDTO {
    Integer id;
    String name;
    String description;
    WorkspaceType type;
    String floor;

    Long availableWorkplaces;
    Long totalWorkplaces;


    public static WorkspaceResponseDTO of(final Workspace workspace) {
        return WorkspaceResponseDTO.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .type(workspace.getType())
                .floor(workspace.getFloor())
                .build();
    }

    public static WorkspaceResponseDTO of(
            final Workspace workspace,
            final Pair<Long, Long> availableOnTotalWorkplaces
    ) {
        return WorkspaceResponseDTO.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .type(workspace.getType())
                .floor(workspace.getFloor())
                .availableWorkplaces(availableOnTotalWorkplaces.getFirst())
                .totalWorkplaces(availableOnTotalWorkplaces.getSecond())
                .build();
    }

}
