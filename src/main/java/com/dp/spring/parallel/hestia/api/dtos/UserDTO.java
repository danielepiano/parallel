package com.dp.spring.parallel.hestia.api.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {
    @NotNull private Integer id;
    @NotNull private String firstName;
    @NotNull private String lastName;
    @NotNull private String role;
}