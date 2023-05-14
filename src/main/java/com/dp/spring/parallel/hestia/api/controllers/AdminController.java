package com.dp.spring.parallel.hestia.api.controllers;

import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.ROLE_ADMIN_VALUE;

@RequestMapping("/api/v1/admins")
public interface AdminController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured(ROLE_ADMIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    void register(
            @RequestBody RegistrationRequestDTO adminToRegister
    );


    @DeleteMapping(path = "/{userId}")
    @Secured(ROLE_ADMIN_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void disable(
            @PathVariable("userId") Integer userId
    );

}
