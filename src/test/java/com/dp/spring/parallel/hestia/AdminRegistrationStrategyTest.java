package com.dp.spring.parallel.hestia;

import com.dp.spring.parallel.common.exceptions.EmailAlreadyExistsException;
import com.dp.spring.parallel.common.fixtures.UserFixture;
import com.dp.spring.parallel.hermes.services.notification.impl.EmailNotificationService;
import com.dp.spring.parallel.hestia.api.dtos.RegistrationRequestDTO;
import com.dp.spring.parallel.hestia.database.entities.AdminUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AdminRegistrationStrategyTest {

    @Mock
    EmailNotificationService emailNotificationService;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    @Spy
    AdminRegistrationStrategy adminRegistrationStrategy = new AdminRegistrationStrategy(emailNotificationService, userRepository, passwordEncoder);


    @BeforeEach
    public void setUp() {
    }

    @Test
    void buildUser_shouldMap() throws Exception {
        String encodedPassword = "encoded-password";
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

        var buildUser = AdminRegistrationStrategy.class
                .getDeclaredMethod("buildUser", String.class, Integer.class, RegistrationRequestDTO.class);
        buildUser.setAccessible(true);
        AdminUser result = (AdminUser) buildUser.invoke(adminRegistrationStrategy, encodedPassword, scopeId, request);

        assertEquals(request.getFirstName(), result.getFirstName(), "wrong mapping");
        assertEquals(request.getLastName(), result.getLastName(), "wrong mapping");
        assertEquals(request.getBirthDate(), result.getBirthDate(), "wrong mapping");
        assertEquals(request.getPhoneNumber(), result.getPhoneNumber(), "wrong mapping");
        assertEquals(request.getCity(), result.getCity(), "wrong mapping");
        assertEquals(request.getAddress(), result.getAddress(), "wrong mapping");
        assertEquals(request.getEmail(), result.getEmail(), "wrong mapping");
        assertEquals(encodedPassword, result.getPassword(), "wrong mapping");
    }

    @Test
    void register_whenEmailConflict_shouldThrow() {
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

        doReturn(ofNullable(UserFixture.getCompanyManager()))
                .when(userRepository)
                .findByEmail(anyString());

        assertThrows(EmailAlreadyExistsException.class, () -> adminRegistrationStrategy.register(scopeId, request));
    }

    @Test
    void register_whenOk_shouldWork() {
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

        doReturn(Optional.empty())
                .when(userRepository)
                .findByEmail(anyString());
        doReturn(UserFixture.getAdmin())
                .when(userRepository)
                .save(any(User.class));
        doReturn("message")
                .when(emailNotificationService)
                .buildMessage( anyString(), anyMap());
        doNothing().when(emailNotificationService).notify(any(), anyString(), anyString());

        assertDoesNotThrow(() -> adminRegistrationStrategy.register(scopeId, request));
    }

}
