package com.dp.spring.parallel.hestia.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Builder
@Value
@RequiredArgsConstructor
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
