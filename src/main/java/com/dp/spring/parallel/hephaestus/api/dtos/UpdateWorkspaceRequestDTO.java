package com.dp.spring.parallel.hephaestus.api.dtos;

import com.dp.spring.parallel.hephaestus.database.enums.WorkspaceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateWorkspaceRequestDTO {

    @NotBlank
    String name;

    String description;

    @NotNull
    WorkspaceType type;

    String floor;
    Integer maxSeats;

}
