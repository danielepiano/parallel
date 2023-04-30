package com.dp.spring.parallel.hestia.services.registration_strategies;

import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.AdminUser;
import com.dp.spring.parallel.hestia.services.RegistrationService;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AdminUser} registration, following {@link RegistrationService} pattern.
 */
@Service
public class AdminRegistrationStrategy extends RegistrationService<AdminUser> {
    @Override
    protected AdminUser buildUser(final String encodedPassword, final Integer scopeId, final RegistrationRequestDTO dto) {
        return AdminUser.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .city(dto.getCity())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(encodedPassword)
                .build();
    }
}
