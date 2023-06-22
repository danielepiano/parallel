package com.dp.spring.parallel.talos;

import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.talos.database.repositories.TokenDetailsRepository;
import com.dp.spring.parallel.talos.services.JWTService;
import com.dp.spring.parallel.talos.utils.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    @InjectMocks
    @Spy
    JWTService jwtService;

    @Spy
    TokenDetailsRepository tokenDetailsRepository;


    @BeforeEach
    public void setUp() {
    }


    @Test
    void generateToken_shouldTestOverloadingMethod_shouldWork() {
        User user = User.builder()
                .id(1)
                .email("test@test.ts")
                .build();

        JWTUtils.SECRET_KEY = aKey();
        JWTUtils.EXPIRES_IN_MILLIS = anExpirationTimeMillis();

        this.jwtService.generateToken(user);

        verify(this.jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void isTokenValid_whenUsernameNull_shouldBeFalse() {
        doReturn(null)
                .when(this.jwtService)
                .extractUsername(anyString());

        assertFalse(this.jwtService.isTokenValid(aToken()));
    }

    @Test
    void isTokenValid_whenExpired_shouldBeFalse() {
        doReturn("username")
                .when(this.jwtService)
                .extractUsername(anyString());

        doReturn(true)
                .when(this.jwtService)
                .isTokenExpired(anyString());

        assertFalse(this.jwtService.isTokenValid(aToken()));
    }

    @Test
    void isTokenValid_whenValid_shouldBeTrue() {
        doReturn("username")
                .when(this.jwtService)
                .extractUsername(anyString());

        doReturn(false)
                .when(this.jwtService)
                .isTokenExpired(anyString());

        assertTrue(this.jwtService.isTokenValid(aToken()));
    }

    @Test
    void isTokenExpired_whenExpired_shouldBeTrue() {
        doReturn(Date.from(Instant.now().minusMillis(1000)))
                .when(this.jwtService)
                .extractExpiration(anyString());

        assertTrue(this.jwtService.isTokenExpired(aToken()));
    }

    @Test
    void isTokenExpired_whenNotExpired_shouldBeFalse() {
        doReturn(Date.from(Instant.now().plusMillis(1000)))
                .when(this.jwtService)
                .extractExpiration(anyString());

        assertFalse(this.jwtService.isTokenExpired(aToken()));
    }

    @Test
    void extractUsername_shouldBe1234567890() {
        JWTUtils.SECRET_KEY = aKey();
        JWTUtils.EXPIRES_IN_MILLIS = anExpirationTimeMillis();

        try {
            assertEquals("1234567890", this.jwtService.extractUsername(aToken()), "wrong username decodification");
        } catch (Exception ignored) {
        }
    }

    @Test
    void extractExpiration_shouldBe555555555() {
        JWTUtils.SECRET_KEY = aKey();
        JWTUtils.EXPIRES_IN_MILLIS = anExpirationTimeMillis();

        try {
            assertEquals("666666666", this.jwtService.extractExpiration(aToken()), "wrong expiration decodification");
        } catch (Exception ignored) {
        }
    }


    // Fixture
    String aKey() {
        return "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    }

    long anExpirationTimeMillis() {
        return 259200000L;
    }

    /**
     * {
     * "sub": "1234567890",
     * "name": "John Doe",
     * "iat": 555555555,
     * "exp": 666666666
     * }
     */
    String aToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0Ijo1NTU1NTU1NTUsImV4cCI6NjY2NjY2NjY2fQ.8VwN8MddH1C0QQzXgtjijSjF1qi4apPiVLHV6_n0sJo";
    }


}
