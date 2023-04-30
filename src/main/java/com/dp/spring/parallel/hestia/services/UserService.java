package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.services.registration_strategies.AdminRegistrationStrategy;
import com.dp.spring.parallel.hestia.services.registration_strategies.CompanyManagerRegistrationStrategy;
import com.dp.spring.parallel.hestia.services.registration_strategies.EmployeeRegistrationStrategy;
import com.dp.spring.parallel.hestia.services.registration_strategies.HeadquartersReceptionistRegistrationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Generic {@link User} services.
 */
@Service
@RequiredArgsConstructor
public class UserService extends BusinessService {
    private final AdminRegistrationStrategy adminRegistrationStrategy;
    private final CompanyManagerRegistrationStrategy companyManagerRegistrationStrategy;
    private final HeadquartersReceptionistRegistrationStrategy headquartersReceptionistRegistrationStrategy;
    private final EmployeeRegistrationStrategy employeeRegistrationStrategy;


    public void registerAdmin(
            final RegistrationRequestDTO toRegister
    ) {
        this.callRegistrationService(toRegister, adminRegistrationStrategy);
    }

    public void registerCompanyManager(
            final Integer companyId,
            final RegistrationRequestDTO toRegister
    ) {
        checkCompanyExistenceOrThrow(companyId);
        checkPrincipalScopeOrThrow(companyId);
        this.callRegistrationService(companyId, toRegister, companyManagerRegistrationStrategy);
    }

    public void registerHeadquartersReceptionist(
            final Integer companyId,
            final Integer headquartersId,
            final RegistrationRequestDTO toRegister
    ) {
        checkHeadquartersExistenceOrThrow(headquartersId, companyId);
        checkPrincipalScopeOrThrow(companyId);
        this.callRegistrationService(companyId, toRegister, headquartersReceptionistRegistrationStrategy);
    }

    public void registerEmployee(
            final Integer companyId,
            final RegistrationRequestDTO toRegister
    ) {
        checkCompanyExistenceOrThrow(companyId);
        checkPrincipalScopeOrThrow(companyId);
        this.callRegistrationService(companyId, toRegister, employeeRegistrationStrategy);
    }


    /**
     * User creation when no resource scope id is defined.<br>
     * Read also {@link #callRegistrationService(Integer, RegistrationRequestDTO, RegistrationService)}.
     *
     * @param toRegister          the user to register
     * @param registrationService the strategy to use for registration
     * @param <U>                 the type of the user to register, related to its role
     */
    private <U extends User> void callRegistrationService(
            final RegistrationRequestDTO toRegister,
            final RegistrationService<U> registrationService
    ) {
        this.callRegistrationService(null, toRegister, registrationService);
    }

    /**
     * User creation according to the {@link RegistrationService} implementation passed as a parameter.
     *
     * @param scopeId             the id of the resource to attach the user to (e.g. company id, headquarters id, etc.)
     * @param toRegister          the user to register
     * @param registrationService the strategy to use for registration
     * @param <U>                 the type of the user to register, related to its role
     */
    private <U extends User> void callRegistrationService(
            final Integer scopeId,
            final RegistrationRequestDTO toRegister,
            final RegistrationService<U> registrationService
    ) {
        registrationService.register(scopeId, toRegister);
    }

}
