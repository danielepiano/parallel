package com.dp.spring.parallel.talos.api.controllers;

import com.dp.spring.parallel.talos.api.dtos.AccessTokenDTO;
import com.dp.spring.parallel.talos.api.dtos.LoginRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/api/v1/auth")
public interface AuthenticationController {
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    AccessTokenDTO authenticate(
            @RequestBody LoginRequestDTO loginForm
    );
}
