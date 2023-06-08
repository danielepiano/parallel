package com.dp.spring.parallel.hephaestus.api.dtos;

import com.dp.spring.parallel.hephaestus.database.entities.Workplace;
import com.dp.spring.parallel.hephaestus.database.enums.WorkplaceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
                .build();
    }

    public static WorkplaceResponseDTO withWorkspace(final Workplace workplace) {
        return WorkplaceResponseDTO.builder()
                .id(workplace.getId())
                .name(workplace.getName())
                .description(workplace.getDescription())
                .type(workplace.getType())
                .workspace(WorkspaceResponseDTO.of(workplace.getWorkspace()))
                .build();
    }

}
