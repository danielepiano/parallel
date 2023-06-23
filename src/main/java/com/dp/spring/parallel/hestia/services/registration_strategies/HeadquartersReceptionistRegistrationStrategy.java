package com.dp.spring.parallel.hestia.services.registration_strategies;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.services.RegistrationService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link HeadquartersReceptionistUser} registration, following {@link RegistrationService} pattern.
 */
@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadquartersReceptionistRegistrationStrategy extends RegistrationService<HeadquartersReceptionistUser> {
    private HeadquartersService headquartersService;

    private static final String NOTIFICATION_MESSAGE_PATH = "email/headquarters-receptionist-first-access-credentials-template.html";


    public HeadquartersReceptionistRegistrationStrategy(
            @Autowired EmailNotificationService emailNotificationService,
            @Autowired UserRepository userRepository,
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired HeadquartersService headquartersService
    ) {
        super(emailNotificationService, userRepository, passwordEncoder);
        this.headquartersService = headquartersService;
    }

    @Override
    protected HeadquartersReceptionistUser buildUser(
            final String encodedPassword,
            final Integer scopeId,
            final RegistrationRequestDTO dto
    ) {
        final Headquarters headquarters = this.headquartersService.headquarters(scopeId);

        return HeadquartersReceptionistUser.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .city(dto.getCity())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(encodedPassword)
                // following: headquarters receptionist fields
                .headquarters(headquarters)
                .build();
    }

    @Override
    protected String buildNotificationMessage(final String generatedPassword, final HeadquartersReceptionistUser user) {
        return super.buildEmailNotificationMessageFromTemplate(NOTIFICATION_MESSAGE_PATH, generatedPassword, user);
    }
}
