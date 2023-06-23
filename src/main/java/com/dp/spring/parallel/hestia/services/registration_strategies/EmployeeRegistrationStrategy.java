package com.dp.spring.parallel.hestia.services.registration_strategies;

import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.services.CompanyService;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.EmployeeUser;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.services.RegistrationService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link EmployeeUser} registration, following {@link RegistrationService} pattern.
 */
@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeRegistrationStrategy extends RegistrationService<EmployeeUser> {
    private CompanyService companyService;


    public EmployeeRegistrationStrategy(
            @Autowired EmailNotificationService emailNotificationService,
            @Autowired UserRepository userRepository,
            @Autowired PasswordEncoder passwordEncoder,
            CompanyService companyService
    ) {
        super(emailNotificationService, userRepository, passwordEncoder);
        this.companyService = companyService;
    }

    @Override
    protected EmployeeUser buildUser(
            final String encodedPassword,
            final Integer scopeId,
            final RegistrationRequestDTO dto
    ) {
        final Company company = this.companyService.company(scopeId);

        return EmployeeUser.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .city(dto.getCity())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(encodedPassword)
                // following: employee fields
                .company(company)
                .jobPosition(dto.getJobPosition())
                .build();
    }
}
