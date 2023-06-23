package com.dp.spring.parallel.hestia;

import com.dp.spring.parallel.common.exceptions.UserNotFoundException;
import com.dp.spring.parallel.common.exceptions.WrongPasswordException;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.api.dtos.ChangePasswordRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.api.dtos.UpdatePersonalDataRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.services.impl.UserServiceImpl;
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
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    EmailNotificationService emailNotificationService;
    @Spy
    UserRepository userRepository;
    @Spy
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Spy
    AdminRegistrationStrategy adminRegistrationStrategy = new AdminRegistrationStrategy(emailNotificationService, userRepository, passwordEncoder);

    @InjectMocks
    @Spy
    UserServiceImpl userService;


    // Mock
    void mockGetPrincipal(User user) {
        doReturn(user)
                .when(userService)
                .getPrincipalOrThrow();
    }


    @BeforeEach
    public void setUp() {
    }

    @Test
    void register_shouldCallOverloadedRegister() {
        RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                .firstName("admin")
                .lastName("admin")
                .email("admin")
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .build();

        doNothing().when(userService).register(any(), any(RegistrationRequestDTO.class), eq(adminRegistrationStrategy));

        userService.register(request, adminRegistrationStrategy);

        verify(userService).register(any(), any(RegistrationRequestDTO.class), eq(adminRegistrationStrategy));
    }

    @Test
    void register_shouldCallStrategy() {
        Integer scopeId = null;
        RegistrationRequestDTO request = RegistrationRequestDTO.builder()
                .firstName("admin")
                .lastName("admin")
                .email("admin")
                .birthDate(LocalDate.now())
                .phoneNumber("number")
                .city("city")
                .address("address")
                .build();

        doNothing().when(adminRegistrationStrategy).register(any(), any(RegistrationRequestDTO.class));

        userService.register(scopeId, request, adminRegistrationStrategy);

        verify(adminRegistrationStrategy).register(any(), any(RegistrationRequestDTO.class));
    }

    @Test
    void user_whenNotFound_shouldThrow() {
        Integer userId = 1;

        doReturn(Optional.empty())
                .when(userRepository)
                .findById(anyInt());

        assertThrows(UserNotFoundException.class, () -> userService.user(userId));
    }

    @Test
    void user_whenFound_shouldWork() {
        Integer userId = 1;

        doReturn(ofNullable(UserFixture.getAdmin()))
                .when(userRepository)
                .findById(anyInt());

        assertDoesNotThrow(() -> userService.user(userId));
    }

    @Test
    void whoAmI_shouldCallGetPrincipal() {
        doReturn(UserFixture.getAdmin()).when(userService).getPrincipalOrThrow();

        userService.whoAmI();

        verify(userService).getPrincipalOrThrow();
    }

    @Test
    void updatePersonalData_shouldWork() {
        UpdatePersonalDataRequestDTO request = UpdatePersonalDataRequestDTO.builder()
                .phoneNumber("number")
                .city("city")
                .address("address")
                .build();

        doReturn(UserFixture.getAdmin()).when(userService).getPrincipalOrThrow();

        userService.updatePersonalData(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_whenWrongPassword_shouldThrow() {
        ChangePasswordRequestDTO request = ChangePasswordRequestDTO.builder()
                .current("wrong-password")
                .updated("new-password")
                .confirm("new-password")
                .build();

        mockGetPrincipal(UserFixture.getAdmin());
        doReturn(false).when(passwordEncoder).matches(any(), anyString());

        assertThrows(WrongPasswordException.class,() -> userService.changePassword(request));
    }

    @Test
    void changePassword_whenWrongPassword_should() {
        ChangePasswordRequestDTO request = ChangePasswordRequestDTO.builder()
                .current("password")
                .updated("new-password")
                .confirm("new-password")
                .build();

        mockGetPrincipal(UserFixture.getAdmin());
        doReturn(true).when(passwordEncoder).matches(any(), anyString());
        doReturn("message").when(emailNotificationService).buildMessage(anyString(), anyMap());

        userService.changePassword(request);

        verify(passwordEncoder).encode(any());
        verify(userRepository).save(any(User.class));
        verify(emailNotificationService).buildMessage(anyString(), anyMap());
        verify(emailNotificationService).notify(any(), anyString(), anyString());
    }

}
