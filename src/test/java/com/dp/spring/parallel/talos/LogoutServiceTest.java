package com.dp.spring.parallel.talos;

import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.talos.database.entities.TokenDetails;
import com.dp.spring.parallel.talos.database.enums.TokenType;
import com.dp.spring.parallel.talos.database.repositories.TokenDetailsRepository;
import com.dp.spring.parallel.talos.services.LogoutService;
import com.dp.spring.parallel.talos.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {

    @InjectMocks
    LogoutService logoutService;

    @Spy
    TokenDetailsRepository tokenDetailsRepository;
    @Mock
    HttpServletRequest request;


    @BeforeEach
    public void setUp() {
    }

    @Test
    void logout_whenNullAuthHeader_shouldNoOp() {
        doReturn(null)
                .when(this.request)
                .getHeader(anyString());

        this.logoutService.logout(this.request, null, null);

        verify(this.tokenDetailsRepository, times(0)).findByToken(anyString());
    }

    @Test
    void logout_whenNotBearerAuthHeader_shouldNoOp() {
        doReturn(RandomStringUtils.random(32))
                .when(request)
                .getHeader(anyString());

        this.logoutService.logout(request, null, null);

        verify(tokenDetailsRepository, times(0)).findByToken(anyString());
    }

    @Test
    void logout_whenNotBearerAuthHeader_shouldLogout() {
        doReturn(JWTUtils.BEARER_PREFIX + RandomStringUtils.random(32))
                .when(request)
                .getHeader(anyString());

        doAnswer(invocation -> ofNullable(TokenDetails.builder()
                .token(invocation.getArgument(0))
                .tokenType(TokenType.BEARER)
                .user(new User())
                .revoked(false)
                .build())
        ).when(this.tokenDetailsRepository)
                .findByToken(anyString());

        this.logoutService.logout(request, null, null);

        verify(tokenDetailsRepository, times(1)).findByToken(anyString());
        verify(tokenDetailsRepository, times(1)).save(any(TokenDetails.class));
    }
}
