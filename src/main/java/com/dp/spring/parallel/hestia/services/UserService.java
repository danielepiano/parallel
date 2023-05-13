package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.common.exceptions.UserNotFoundException;
import com.dp.spring.parallel.common.services.BusinessService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic {@link User} services.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService extends BusinessService {
    protected UserRepository userRepository;


    /**
     * User creation when no resource scope id is defined.<br>
     * Read also {@link #register(Integer, RegistrationRequestDTO, RegistrationService)}.
     *
     * @param toRegister          the user to register
     * @param registrationService the strategy to use for registration
     * @param <U>                 the type of the user to register, related to its role
     */
    protected <U extends User> void register(
            final RegistrationRequestDTO toRegister,
            final RegistrationService<U> registrationService
    ) {
        this.register(null, toRegister, registrationService);
    }

    /**
     * User creation according to the {@link RegistrationService} implementation passed as a parameter.
     *
     * @param scopeId             the id of the resource to attach the user to (e.g. company id, headquarters id, etc.)
     * @param toRegister          the user to register
     * @param registrationService the strategy to use for registration
     * @param <U>                 the type of the user to register, related to its role
     */
    protected <U extends User> void register(
            final Integer scopeId,
            final RegistrationRequestDTO toRegister,
            final RegistrationService<U> registrationService
    ) {
        registrationService.register(scopeId, toRegister);
    }


    public User user(final Integer id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserRole.ANY.getRole(), id));
    }
}
