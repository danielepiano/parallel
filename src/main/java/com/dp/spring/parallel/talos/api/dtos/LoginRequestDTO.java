package com.dp.spring.parallel.talos.api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Builder
@Value
@RequiredArgsConstructor
public class LoginRequestDTO {
    @NotBlank
    String email;

    @NotBlank
    String password;
}
