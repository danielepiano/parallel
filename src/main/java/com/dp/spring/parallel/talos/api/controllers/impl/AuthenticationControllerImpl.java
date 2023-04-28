package com.dp.spring.parallel.talos.api.controllers.impl;

import com.dp.spring.parallel.talos.api.controllers.AuthenticationController;
import com.dp.spring.parallel.talos.api.dtos.AccessTokenDTO;
import com.dp.spring.parallel.talos.api.dtos.LoginRequestDTO;
import com.dp.spring.parallel.talos.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {
    private final AuthenticationService authenticationService;

    @Override
    public AccessTokenDTO authenticate(
            final LoginRequestDTO loginForm
    ) {
        return this.authenticationService.authenticate(loginForm);
    }
}
