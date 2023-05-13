package com.dp.spring.parallel.hephaestus.api.controllers;

import com.dp.spring.parallel.hephaestus.api.dtos.HeadquartersResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.*;

@RequestMapping("/api/v1/headquarters")
public interface HeadquartersController {

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    HeadquartersResponseDTO headquarters(
            @PathVariable("id") Integer id
    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE, ROLE_EMPLOYEE_VALUE})
    Set<HeadquartersResponseDTO> headquarters();
}
