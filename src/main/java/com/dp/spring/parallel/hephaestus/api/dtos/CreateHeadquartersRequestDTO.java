package com.dp.spring.parallel.hephaestus.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateHeadquartersRequestDTO {
    @NotBlank
    String city;
    @NotBlank
    String address;
    @NotBlank
    String phoneNumber;

    String feDescription;
}
