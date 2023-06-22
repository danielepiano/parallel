package com.dp.spring.parallel.talos;

import com.dp.spring.parallel.common.exceptions.EmailNotFoundException;
import com.dp.spring.parallel.common.exceptions.WrongCredentialsException;
import com.dp.spring.parallel.hestia.database.entities.AdminUser;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.talos.api.dtos.AccessTokenDTO;
import com.dp.spring.parallel.talos.api.dtos.LoginRequestDTO;
import com.dp.spring.parallel.talos.services.AuthenticationService;
import com.dp.spring.parallel.talos.services.JWTService;
import com.dp.spring.parallel.talos.services.TokenDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    AuthenticationService authenticationService;

    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TokenDetailsService tokenDetailsService;
    @Mock
    JWTService jwtService;
    @Mock
    UserRepository userRepository;

    @Mock
    ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
    }

    @Test
    void authenticate_whenAuthenticationException_throwWrongCredentialsException() {
        LoginRequestDTO login = LoginRequestDTO.builder()
                .email("email")
                .password("pwd")
                .build();

        mockAuthenticationException_on_authenticationManager_authenticate();

        assertThrows(WrongCredentialsException.class, () -> this.authenticationService.authenticate(login));
    }

    @Test
    void authenticate_whenUserNotFound_throwEmailNotFoundException() {
        LoginRequestDTO login = LoginRequestDTO.builder()
                .email("email")
                .password("pwd")
                .build();

        mockUserNotFound_on_userRepository_findByEmail();

        assertThrows(EmailNotFoundException.class, () -> this.authenticationService.authenticate(login));
    }

    @Test
    void authenticate_whenTokenGenerated_returnAccessTokenDTO() {
        LoginRequestDTO login = LoginRequestDTO.builder()
                .email("email")
                .password("pwd")
                .build();

        mockUser_on_userRepository_findByEmail();
        String token = mockToken_on_jwtService_generateToken();

        assertEquals(AccessTokenDTO.builder().token(token).build(), this.authenticationService.authenticate(login));
    }


    // ---------------------- MOCKS ---------------------- //
    // authenticationManager.authenticate
    void mockAuthenticationException_on_authenticationManager_authenticate() {
        doThrow(BadCredentialsException.class)
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    // userRepository.findByEmail
    void mockUserNotFound_on_userRepository_findByEmail() {
        doReturn(empty())
                .when(userRepository)
                .findByEmail(anyString());
    }

    void mockUser_on_userRepository_findByEmail() {
        doReturn(
                Optional.of(
                        AdminUser.builder()
                                .id(1)
                                .email("email")
                                .password("pwd")
                                .role(UserRole.ADMIN)
                                .build()
                )).when(userRepository)
                .findByEmail(anyString());
    }

    // jwtService.generateToken
    String mockToken_on_jwtService_generateToken() {
        String token = "abcdefghijklmnopqrstuvwxyz";
        doReturn(token)
                .when(jwtService)
                .generateToken(Mockito.isNull(), any(User.class));
        return token;
    }
}
