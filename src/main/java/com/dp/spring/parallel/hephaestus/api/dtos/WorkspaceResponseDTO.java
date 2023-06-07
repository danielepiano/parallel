package com.dp.spring.parallel.hephaestus.api.dtos;

import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.entities.Workspace;
import com.dp.spring.parallel.hephaestus.database.enums.WorkplaceType;
import com.dp.spring.parallel.hephaestus.database.enums.WorkspaceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.util.Collection;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

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
    Integer maxSeats;

    Set<WorkplaceResponseDTO> workplaces;

    // Used for mapping of collections
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

    // Used for detail mapping
    public static WorkspaceResponseDTO withWorkplaces(final Workspace workspace) {
        return WorkspaceResponseDTO.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .description(workspace.getDescription())
                .type(workspace.getType())
                .floor(workspace.getFloor())
                .maxSeats(workspace.getMaxSeats())
                .workplaces(
                        ofNullable(workspace.getWorkplaces()).stream()
                                .flatMap(Collection::stream)
                                .map(WorkplaceResponseDTO::of)
                                .collect(toSet())
                )
                .build();
    }

    @Builder
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WorkplaceResponseDTO {
        Integer id;
        String name;
        String description;
        WorkplaceType type;

        public static WorkplaceResponseDTO of(final Workplace workplace) {
            return WorkplaceResponseDTO.builder()
                    .id(workplace.getId())
                    .name(workplace.getName())
                    .description(workplace.getDescription())
                    .type(workplace.getType())
                    .build();
        }
    }
}
