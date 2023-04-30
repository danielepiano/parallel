package com.dp.spring.parallel.hestia.services.registration_strategies;

import com.dp.spring.parallel.common.exceptions.CompanyNotFoundException;
import com.dp.spring.parallel.hephaestus.database.entities.Company;
import com.dp.spring.parallel.hephaestus.database.repositories.CompanyRepository;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.CompanyManagerUser;
import com.dp.spring.parallel.hestia.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link CompanyManagerUser} registration, following {@link RegistrationService} pattern.
 */
@Service
@RequiredArgsConstructor
public class CompanyManagerRegistrationStrategy extends RegistrationService<CompanyManagerUser> {
    private final CompanyRepository companyRepository;


    @Override
    protected CompanyManagerUser buildUser(
            final String encodedPassword,
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
                .password(encodedPassword)
                // following: company manager fields
                .company(company)
                .jobPosition(dto.getJobPosition())
                .build();
    }
}
