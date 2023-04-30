package com.dp.spring.parallel.hestia.api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Jacksonized
@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequestDTO {
    @NotBlank @Length(min = 1, max = 30)
    String firstName;
    @NotBlank @Length(min = 1, max = 30)
    String lastName;

    @NotNull
    LocalDate birthDate;

    // custom
    String phoneNumber;
    // custom
    String city;
    // custom
    String address;

    // group rule?
    String jobPosition;

    @NotBlank @Email
    String email;
}
