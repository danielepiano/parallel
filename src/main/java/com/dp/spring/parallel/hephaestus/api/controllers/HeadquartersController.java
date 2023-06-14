package com.dp.spring.parallel.hephaestus.api.controllers;

import com.dp.spring.parallel.hephaestus.api.dtos.HeadquartersResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.*;

@RequestMapping("/api/v1/headquarters")
public interface HeadquartersController {

    @GetMapping(path = "/{headquartersId}", produces = MediaType.APPLICATION_JSON_VALUE)
    HeadquartersResponseDTO headquarters(
            @PathVariable("headquartersId") Integer headquartersId
    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE, ROLE_EMPLOYEE_VALUE})
    List<HeadquartersResponseDTO> headquarters();


    @PatchMapping(path = "/favs/{headquartersId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_COMPANY_MANAGER_VALUE, ROLE_EMPLOYEE_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void toggleFavouriteHeadquarters(
            @PathVariable("headquartersId") Integer headquartersId
    );

}
