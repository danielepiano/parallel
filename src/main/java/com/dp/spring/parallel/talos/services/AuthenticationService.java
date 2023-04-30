package com.dp.spring.parallel.talos.services;

import com.dp.spring.parallel.common.exceptions.EmailNotFoundException;
import com.dp.spring.parallel.common.exceptions.WrongCredentialsException;
import com.dp.spring.parallel.hestia.database.entities.*;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.talos.api.dtos.AccessTokenDTO;
import com.dp.spring.parallel.talos.api.dtos.LoginRequestDTO;
import com.dp.spring.parallel.talos.api.dtos.UserToken;
import com.dp.spring.parallel.talos.database.enums.TokenType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final TokenDetailsService tokenDetailsService;
    private final JWTService jwtService;

    private final UserRepository<User> userRepository;
    private final UserRepository<AdminUser> adminUserRepository;
    private final UserRepository<CompanyManagerUser> companyManagerUserRepository;
    private final UserRepository<HeadquartersReceptionistUser> headquartersReceptionistUserRepository;
    private final UserRepository<EmployeeUser> employeeUserRepository;

    private final ObjectMapper objectMapper;


    /**
     * User authentication via username and password and JWT generation if login successful.
     *
     * @param loginForm the credentials submitted
     * @return the access token
     * @throws AuthenticationException in case of wrong credentials
     */
    public AccessTokenDTO authenticate(final LoginRequestDTO loginForm) throws AuthenticationException {
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginForm.getEmail(), loginForm.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new WrongCredentialsException();
        }

        // Case: valid credentials
        var user = this.userRepository.findByEmail(loginForm.getEmail())
                .orElseThrow(() -> new EmailNotFoundException(loginForm.getEmail()));

        // Invalidate every current valid token for the authenticated user, and generate a new valid one
        this.tokenDetailsService.revokeAllTokensForUser(user);
        var extraClaims = this.extractExtraClaimsFromUser(user);
        var token = this.jwtService.generateToken(extraClaims, user);
        this.tokenDetailsService.createTokenDetails(token, TokenType.BEARER, user);

        return new AccessTokenDTO(token);
    }


    private Map<String, Object> extractExtraClaimsFromUser(final User user) {
        final UserToken userToken = switch (user.getRole()) {
            case ADMIN -> UserToken.of(adminUserRepository.getReferenceById(user.getId()));
            case COMPANY_MANAGER -> UserToken.of(companyManagerUserRepository.getReferenceById(user.getId()));
            case HEADQUARTERS_RECEPTIONIST ->
                    UserToken.of(headquartersReceptionistUserRepository.getReferenceById(user.getId()));
            case EMPLOYEE -> UserToken.of(employeeUserRepository.getReferenceById(user.getId()));
            default -> UserToken.of(user);
        };

        return this.objectMapper.convertValue(userToken, new TypeReference<Map<String, Object>>() {
        });
    }
}
