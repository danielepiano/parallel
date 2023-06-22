package com.dp.spring.parallel.talos.services;

import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.talos.database.entities.TokenDetails;
import com.dp.spring.parallel.talos.database.enums.TokenType;
import com.dp.spring.parallel.talos.database.repositories.TokenDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenDetailsService {
    private final TokenDetailsRepository tokenDetailsRepository;

    /**
     * Creation of a new {@link TokenDetails} record related to the specified token.
     *
     * @param token the token
     * @param type  the token type
     * @param user  the user for whom the token is valid
     */
    public void createTokenDetails(final String token, final TokenType type, final User user) {
        var newTokenDetails = TokenDetails.builder()
                .token(token)
                .tokenType(type)
                .user(user)
                .build();
        this.tokenDetailsRepository.save(newTokenDetails);
    }

    /**
     * Revocation of each token for a specified user.
     *
     * @param user the user whose token need to be revoked
     */
    public void revokeAllTokensForUser(final User user) {
        var validUserTokensDetails = this.tokenDetailsRepository.findAllValidTokensByUserId(user.getId());

        validUserTokensDetails.forEach(tokenDetails -> tokenDetails.setRevoked(true));

        this.tokenDetailsRepository.saveAll(validUserTokensDetails);
    }
}
