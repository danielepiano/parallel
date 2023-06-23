package com.dp.spring.parallel.hestia;

import com.dp.spring.parallel.common.exceptions.UserNotDeletableException;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.repositories.AdminUserRepository;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.services.impl.AdminUserServiceImpl;
import com.dp.spring.parallel.hestia.services.registration_strategies.AdminRegistrationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    EmailNotificationService emailNotificationService;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Spy
    AdminRegistrationStrategy adminRegistrationStrategy = new AdminRegistrationStrategy(emailNotificationService, userRepository, passwordEncoder);
    @Spy
    AdminUserRepository adminUserRepository;

    @InjectMocks
    @Spy
    AdminUserServiceImpl adminUserService;


    @BeforeEach
    public void setUp() {
    }

    @Test
    void register_shouldCallSuper() {
        RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                .firstName("admin")
                .lastName("admin")
                .email("admin")
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .build();

        doNothing().when(adminUserService).register(any(RegistrationRequestDTO.class), eq(adminRegistrationStrategy));

        adminUserService.register(request);

        verify(adminUserService).register(any(RegistrationRequestDTO.class), eq(adminRegistrationStrategy));
    }

    @Test
    void disable_whenOk_shouldWork() {
        Integer adminId = 1;

        doReturn(List.of(UserFixture.getAdmin(), UserFixture.getAdmin()))
                .when(adminUserRepository)
                .findAll();
        doNothing().when(adminUserRepository).softDeleteById(anyInt());

        adminUserService.disable(adminId);

        verify(adminUserRepository).softDeleteById(anyInt());
    }

    @Test
    void disable_whenNotPossible_shouldThrow() {
        Integer adminId = 1;

        doReturn(List.of(UserFixture.getAdmin()))
                .when(adminUserRepository)
                .findAll();

        assertThrows(UserNotDeletableException.class, () -> adminUserService.disable(adminId));

        verify(adminUserRepository, times(0)).softDeleteById(anyInt());
    }

}
