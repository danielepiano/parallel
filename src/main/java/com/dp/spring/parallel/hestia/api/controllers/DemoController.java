package com.dp.spring.parallel.hestia.api.controllers;

import com.dp.spring.parallel.hestia.database.enums.UserRole;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {
    @GetMapping
    @Secured(UserRole.Constants.ADMIN_VALUE)
    public String hello() {
        return "hello";
    }
}
