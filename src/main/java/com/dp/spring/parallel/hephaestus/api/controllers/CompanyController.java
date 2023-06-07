package com.dp.spring.parallel.hephaestus.api.controllers;

import com.dp.spring.parallel.hephaestus.api.dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.*;

@RequestMapping("/api/v1/companies")
public interface CompanyController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured(ROLE_ADMIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    CompanyResponseDTO add(
            @RequestBody CreateCompanyRequestDTO toAddData
    );

    @GetMapping(path = "/{companyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    CompanyResponseDTO company(
            @PathVariable("companyId") Integer companyId
    );

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    Set<CompanyResponseDTO> companies();

    @PutMapping(
            path = "/{companyId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    CompanyResponseDTO update(
            @PathVariable("companyId") Integer companyId,
            @RequestBody UpdateCompanyRequestDTO updatedData
    );

    @DeleteMapping(path = "/{companyId}")
    @Secured(ROLE_ADMIN_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void remove(
            @PathVariable("companyId") Integer companyId
    );


    @PostMapping(path = "/{companyId}/headquarters", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    CompanyHeadquartersResponseDTO addHeadquarters(
            @PathVariable("companyId") Integer companyId,
            @RequestBody CreateHeadquartersRequestDTO toAddData
    );

    @GetMapping(path = "/{companyId}/headquarters", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE, ROLE_EMPLOYEE_VALUE})
    Set<CompanyHeadquartersResponseDTO> headquarters(
            @PathVariable("companyId") Integer companyId
    );

    @PutMapping(
            path = "/{companyId}/headquarters/{headquartersId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    CompanyHeadquartersResponseDTO update(
            @PathVariable("companyId") Integer companyId,
            @PathVariable("headquartersId") Integer headquartersId,
            @RequestBody UpdateHeadquartersRequestDTO updatedData
    );

    @DeleteMapping(path = "/{companyId}/headquarters/{headquartersId}")
    @Secured({ROLE_ADMIN_VALUE, ROLE_COMPANY_MANAGER_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void removeHeadquarters(
            @PathVariable("companyId") Integer companyId,
            @PathVariable("headquartersId") Integer headquartersId
    );
}
