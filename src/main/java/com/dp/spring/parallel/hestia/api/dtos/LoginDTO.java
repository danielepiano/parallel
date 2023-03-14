package com.dp.spring.parallel.hestia.api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class LoginDTO {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
