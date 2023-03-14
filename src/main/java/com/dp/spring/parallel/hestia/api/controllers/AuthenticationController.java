package com.dp.spring.parallel.hestia.api.controllers;

import com.dp.spring.parallel.hestia.api.dtos.AccessTokenDTO;
import com.dp.spring.parallel.hestia.api.dtos.ExampleRegistrationDTO;
import com.dp.spring.parallel.hestia.api.dtos.LoginDTO;
import com.dp.spring.parallel.hestia.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public void register(
            @RequestBody ExampleRegistrationDTO registrationRequest
    ) {
        this.authenticationService.register(registrationRequest);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AccessTokenDTO> authenticate(
            @RequestBody LoginDTO loginForm
    ) {
        return ResponseEntity.ok( this.authenticationService.authenticate(loginForm) );
    }
}
