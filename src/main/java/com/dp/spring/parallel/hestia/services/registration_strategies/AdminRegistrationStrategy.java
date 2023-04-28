package com.dp.spring.parallel.hestia.services.registration_strategies;

import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.AdminUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.services.RegistrationService;
import com.dp.spring.parallel.mnemosyne.services.email.EmailService;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AdminUser} registration, following {@link RegistrationService} pattern.
 */
@Service
public class AdminRegistrationStrategy extends RegistrationService<AdminUser> {
    public AdminRegistrationStrategy(
            final UserRepository<User> userRepository,
            final UserRepository<AdminUser> adminUserRepository,
            final EmailService emailService
    ) {
        super(userRepository, adminUserRepository, emailService);
    }

    @Override
    protected AdminUser buildUser(final String generatedPassword, final Integer scopeId, final RegistrationRequestDTO dto) {
        return AdminUser.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .city(dto.getCity())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(generatedPassword)
                .build();
    }
}
