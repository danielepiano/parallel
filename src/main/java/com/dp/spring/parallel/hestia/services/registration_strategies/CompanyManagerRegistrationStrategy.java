package com.dp.spring.parallel.hestia.services.registration_strategies;

import com.dp.spring.parallel.common.exceptions.CompanyNotFoundException;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.repositories.CompanyRepository;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.services.RegistrationService;
import com.dp.spring.parallel.mnemosyne.services.email.EmailService;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link CompanyManagerUser} registration, following {@link RegistrationService} pattern.
 */
@Service
public class CompanyManagerRegistrationStrategy extends RegistrationService<CompanyManagerUser> {
    private final CompanyRepository companyRepository;

    public CompanyManagerRegistrationStrategy(
            final UserRepository<User> userRepository,
            final UserRepository<CompanyManagerUser> companyManagerUserRepository,
            final EmailService emailService,
            final CompanyRepository companyRepository
    ) {
        super(userRepository, companyManagerUserRepository, emailService);
        this.companyRepository = companyRepository;
    }

    @Override
    protected CompanyManagerUser buildUser(
            final String generatedPassword,
            final Integer scopeId,
            final RegistrationRequestDTO dto
    ) {
        final Company company = this.companyRepository.findById(scopeId)
                .orElseThrow(() -> new CompanyNotFoundException(scopeId));

        return CompanyManagerUser.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .city(dto.getCity())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(generatedPassword)
                // following: company manager fields
                .company(company)
                .jobPosition(dto.getJobPosition())
                .build();
    }
}
