package com.dp.spring.parallel.hestia.api.controllers.impl;

import com.dp.spring.parallel.hestia.api.controllers.EmployeeController;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.services.EmployeeUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeControllerImpl implements EmployeeController {
    private final EmployeeUserService employeeUserService;


    @Override
    public void register(
            final Integer companyId,
            final RegistrationRequestDTO toRegister
    ) {
        this.employeeUserService.register(companyId, toRegister);
    }

    @Override
    public void disable(
            final Integer companyId,
            Integer userId
    ) {
        this.employeeUserService.disable(companyId, userId);
    }
}
