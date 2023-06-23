package com.dp.spring.parallel.hestia.services.registration_strategies;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.services.CompanyService;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.services.RegistrationService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link CompanyManagerUser} registration, following {@link RegistrationService} pattern.
 */
@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyManagerRegistrationStrategy extends RegistrationService<CompanyManagerUser> {
    private CompanyService companyService;


    public CompanyManagerRegistrationStrategy(
            @Autowired EmailNotificationService emailNotificationService,
            @Autowired UserRepository userRepository,
            @Autowired PasswordEncoder passwordEncoder,
            @Autowired CompanyService companyService
    ) {
        super(emailNotificationService, userRepository, passwordEncoder);
        this.companyService = companyService;
    }


    @Override
    protected CompanyManagerUser buildUser(
            final String encodedPassword,
            final Integer scopeId,
            final RegistrationRequestDTO dto
    ) {
        final Company company = this.companyService.company(scopeId);

        return CompanyManagerUser.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .city(dto.getCity())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(encodedPassword)
                // following: company manager fields
                .company(company)
                .jobPosition(dto.getJobPosition())
                .build();
    }
}
