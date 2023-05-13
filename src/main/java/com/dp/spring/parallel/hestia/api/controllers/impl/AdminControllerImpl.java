package com.dp.spring.parallel.hestia.api.controllers.impl;

import com.dp.spring.parallel.hestia.api.controllers.AdminController;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.services.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {
    private final AdminUserService adminUserService;


    @Override
    public void register(final RegistrationRequestDTO toRegister) {
        this.adminUserService.register(toRegister);
    }


    @Override
    public void disable(final Integer userId) {
        this.adminUserService.disable(userId);
    }

}
