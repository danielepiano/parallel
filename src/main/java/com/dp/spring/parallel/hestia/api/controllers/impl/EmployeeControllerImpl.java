package com.dp.spring.parallel.hestia.api.controllers.impl;

import com.dp.spring.parallel.hestia.api.controllers.EmployeeController;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeControllerImpl implements EmployeeController {
    private final UserService userService;


    @Override
    public void register(
            final Integer companyId,
            final RegistrationRequestDTO toRegister
    ) {
        this.userService.registerEmployee(companyId, toRegister);
    }

}
