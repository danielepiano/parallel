package com.dp.spring.parallel.hestia.api.controllers;

import com.dp.spring.parallel.hestia.api.dtos.ChangePasswordRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.UpdatePersonalDataRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users")
public interface UserController {


    @GetMapping(path = "/who-am-i")
    UserResponseDTO whoAmI();


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
