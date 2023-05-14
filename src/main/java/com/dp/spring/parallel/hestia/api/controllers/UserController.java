package com.dp.spring.parallel.hestia.api.controllers;

import com.dp.spring.parallel.hestia.api.dtos.ChangePasswordRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.UpdatePersonalDataRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/api/v1/users")
public interface UserController {

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updatePersonalData(
            @Valid @RequestBody UpdatePersonalDataRequestDTO updatedData
    );

    @PutMapping(path = "/pwd", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changePassword(
            @Valid @RequestBody ChangePasswordRequestDTO changeRequest
    );

}
