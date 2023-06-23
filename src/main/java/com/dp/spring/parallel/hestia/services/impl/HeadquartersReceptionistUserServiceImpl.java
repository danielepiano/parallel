package com.dp.spring.parallel.hestia.services.impl;

import com.dp.spring.parallel.hephaestus.database.entities.Headquarters;
import com.dp.spring.parallel.hephaestus.services.HeadquartersService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.HeadquartersReceptionistUser;
import com.dp.spring.parallel.hestia.database.repositories.HeadquartersReceptionistUserRepository;
import com.dp.spring.parallel.hestia.services.HeadquartersReceptionistUserService;
import com.dp.spring.parallel.hestia.services.registration_strategies.HeadquartersReceptionistRegistrationStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * {@link HeadquartersReceptionistUserService} implementation.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class HeadquartersReceptionistUserServiceImpl extends UserServiceImpl implements HeadquartersReceptionistUserService {
    private final HeadquartersReceptionistRegistrationStrategy headquartersReceptionistRegistrationStrategy;

    private final HeadquartersService headquartersService;

    private final HeadquartersReceptionistUserRepository headquartersReceptionistUserRepository;


    /**
     * {@inheritDoc}
     *
     * @param companyId      the company of the headquarters
     * @param headquartersId the headquarters of the headquarters receptionist to register
     * @param toRegister     the registration request
     */
    @Override
    public void register(
            Integer companyId,
            Integer headquartersId,
            RegistrationRequestDTO toRegister
    ) {
        this.headquartersService.checkExistence(companyId, headquartersId);
        checkPrincipalScopeOrThrow(companyId);
        register(headquartersId, toRegister, headquartersReceptionistRegistrationStrategy);
    }

    /**
     * {inheritDoc}
     *
     * @param headquarters the headquarters
     * @return the headquarters receptionists of the given headquarters
     */
    public Set<HeadquartersReceptionistUser> headquartersReceptionistsFor(Headquarters headquarters) {
        return this.headquartersReceptionistUserRepository.findAllByHeadquarters(headquarters);
    }


    /**
     * {@inheritDoc}
     *
     * @param companyId      the company of the headquarters
     * @param headquartersId the headquarters of the headquarters receptionist to register
     * @param userId         the id of the headquarters receptionists to disable
     */
    @Override
    public void disable(
            Integer companyId,
            Integer headquartersId,
            Integer userId
    ) {
        this.headquartersService.checkExistence(companyId, headquartersId);
        checkPrincipalScopeOrThrow(companyId);
        this.headquartersReceptionistUserRepository.softDeleteById(userId);
    }

}
