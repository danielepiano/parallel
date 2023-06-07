package com.dp.spring.parallel.hephaestus.api.dtos;

import com.dp.spring.parallel.hephaestus.database.enums.WorkplaceType;
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
public class UpdateWorkplaceRequestDTO {

    @NotBlank
    String name;

    String description;

    @NotNull
    WorkplaceType type;

}