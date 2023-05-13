package com.dp.spring.parallel.hestia.api.controllers.impl;

import com.dp.spring.parallel.hestia.api.controllers.CompanyManagerController;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.services.CompanyManagerUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyManagerControllerImpl implements CompanyManagerController {
    private final CompanyManagerUserService companyManagerUserService;


    @Override
    public void register(
            final Integer companyId,
            final RegistrationRequestDTO toRegister
    ) {
        this.companyManagerUserService.register(companyId, toRegister);
    }


    @Override
    public void disable(
            final Integer companyId,
            final Integer userId
    ) {
        this.companyManagerUserService.disable(companyId, userId);
    }
}
