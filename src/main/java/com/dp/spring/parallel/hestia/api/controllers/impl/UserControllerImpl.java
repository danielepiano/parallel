package com.dp.spring.parallel.hestia.api.controllers.impl;

import com.dp.spring.parallel.hestia.api.controllers.UserController;
import com.dp.spring.parallel.hestia.api.dtos.ChangePasswordRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.UpdatePersonalDataRequestDTO;
import com.dp.spring.parallel.hestia.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    @Qualifier("userService")
    private final UserService userService;


    @Override
    public void updatePersonalData(
            final UpdatePersonalDataRequestDTO updatedData
    ) {
        this.userService.updatePersonalData(updatedData);
    }

    @Override
    public void changePassword(
            final ChangePasswordRequestDTO changeRequest
    ) {
        this.userService.changePassword(changeRequest);
    }
}
