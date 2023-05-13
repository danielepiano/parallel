package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.HeadquartersReceptionistUserRepository;
import com.dp.spring.parallel.hestia.services.registration_strategies.HeadquartersReceptionistRegistrationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Generic {@link User} services.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class HeadquartersReceptionistUserService extends UserService {
    private final HeadquartersReceptionistRegistrationStrategy headquartersReceptionistRegistrationStrategy;
    private final HeadquartersReceptionistUserRepository headquartersReceptionistUserRepository;


    public void register(
            final Integer companyId,
            final Integer headquartersId,
            final RegistrationRequestDTO toRegister
    ) {
        checkHeadquartersExistenceOrThrow(headquartersId, companyId);
        checkPrincipalScopeOrThrow(companyId);
        super.register(headquartersId, toRegister, headquartersReceptionistRegistrationStrategy);
    }

    public Set<HeadquartersReceptionistUser> headquartersReceptionistsFor(final Headquarters headquarters) {
        return this.headquartersReceptionistUserRepository.findAllByHeadquarters(headquarters);
    }

    public void disable(
            final Integer companyId,
            final Integer headquartersId,
            final Integer userId
    ) {
        checkHeadquartersExistenceOrThrow(headquartersId, companyId);
        checkPrincipalScopeOrThrow(companyId);
        this.headquartersReceptionistUserRepository.softDeleteById(userId);
    }
}
