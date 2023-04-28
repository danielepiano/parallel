package com.dp.spring.parallel.hestia.api.controllers;

import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.dp.spring.parallel.hestia.database.enums.UserRole.Constants.ROLE_ADMIN_VALUE;

@RequestMapping("/api/hestia/v1/admins")
public interface AdminController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured(ROLE_ADMIN_VALUE)
    void register(
            @RequestBody RegistrationRequestDTO adminToRegister
    );

}
