package com.dp.spring.parallel.talos.services;

import com.dp.spring.parallel.talos.database.repositories.TokenDetailsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import static com.dp.spring.parallel.talos.utils.JWTUtils.AUTHORIZATION_HEADER_NAME;
import static com.dp.spring.parallel.talos.utils.JWTUtils.BEARER_PREFIX;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    public final static String LOGOUT_URL = "/api/hestia/v1/auth/logout";

    private final TokenDetailsRepository tokenDetailsRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return;
        }

        // If Authorization header is set: extract the token, find it in the db and revoke it
        final String token = authHeader.substring(BEARER_PREFIX.length());

        this.tokenDetailsRepository.findByToken(token)
                .ifPresent(tokenDetails -> {
                    tokenDetails.setRevoked(true);
                    tokenDetailsRepository.save(tokenDetails);
                });
    }
}
