package com.dp.spring.parallel.hestia.api.controllers.impl;

import com.dp.spring.parallel.hestia.api.controllers.HeadquartersReceptionistController;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.services.HeadquartersReceptionistUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeadquartersReceptionistControllerImpl implements HeadquartersReceptionistController {
    private final HeadquartersReceptionistUserService headquartersReceptionistUserService;


    @Override
    public void register(
            final Integer companyId,
            final Integer headquartersId,
            final RegistrationRequestDTO toRegister
    ) {
        this.headquartersReceptionistUserService.register(companyId, headquartersId, toRegister);
    }

    @Override
    public void disable(
            final Integer companyId,
            final Integer headquartersId,
            final Integer userId
    ) {
        this.headquartersReceptionistUserService.disable(companyId, headquartersId, userId);
    }
}
