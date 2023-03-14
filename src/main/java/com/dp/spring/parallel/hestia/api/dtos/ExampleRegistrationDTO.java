package com.dp.spring.parallel.hestia.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ExampleRegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
