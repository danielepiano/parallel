package com.dp.spring.parallel.hestia.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePersonalDataRequestDTO {
    @NotNull
    LocalDate birthDate;
    @NotBlank
    String phoneNumber;
    @NotBlank
    String city;
    @NotBlank
    String address;
}
