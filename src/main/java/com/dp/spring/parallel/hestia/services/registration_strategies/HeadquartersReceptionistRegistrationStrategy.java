package com.dp.spring.parallel.hestia.services.registration_strategies;

import com.dp.spring.parallel.common.exceptions.HeadquartersNotFoundException;
import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.database.repositories.HeadquartersRepository;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link HeadquartersReceptionistUser} registration, following {@link RegistrationService} pattern.
 */
@Service
@RequiredArgsConstructor
public class HeadquartersReceptionistRegistrationStrategy extends RegistrationService<HeadquartersReceptionistUser> {
    private final HeadquartersRepository headquartersRepository;


    @Override
    protected HeadquartersReceptionistUser buildUser(
            final String encodedPassword,
            final Integer scopeId,
            final RegistrationRequestDTO dto
    ) {
        final Headquarters headquarters = this.headquartersRepository.findById(scopeId)
                .orElseThrow(() -> new HeadquartersNotFoundException(scopeId));

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
}
