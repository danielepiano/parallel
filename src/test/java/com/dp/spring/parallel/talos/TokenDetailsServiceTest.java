package com.dp.spring.parallel.talos;

import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.talos.database.entities.TokenDetails;
import com.dp.spring.parallel.talos.database.enums.TokenType;
import com.dp.spring.parallel.talos.database.repositories.TokenDetailsRepository;
import com.dp.spring.parallel.talos.services.TokenDetailsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenDetailsServiceTest {

    @InjectMocks
    TokenDetailsService tokenDetailsService;

    @Spy
    TokenDetailsRepository tokenDetailsRepository;


    @BeforeEach
    public void setUp() {
    }

    @Test
    void createTokenDetails_shouldCallRepository() {
        this.tokenDetailsService.createTokenDetails(RandomStringUtils.random(16), TokenType.BEARER, new User());

        verify(tokenDetailsRepository, times(1)).save(any(TokenDetails.class));
    }

    @Test
    void revokeAllTokensForUser_shouldRevokeEach_shouldCallRepository() {
        User user = User.builder().id(1).build();

        doReturn(List.of(aBearerToken(user), aBearerToken(user)))
                .when(tokenDetailsRepository)
                .findAllValidTokensByUserId(user.getId());

        this.tokenDetailsService.revokeAllTokensForUser(user);

        verify(tokenDetailsRepository, times(1)).saveAll(anyList());
    }

    // Fixture
    TokenDetails aBearerToken(User user) {
        return TokenDetails.builder()
                .token(RandomStringUtils.random(32))
                .tokenType(TokenType.BEARER)
                .user(user)
                .revoked(false)
                .build();
    }
}
