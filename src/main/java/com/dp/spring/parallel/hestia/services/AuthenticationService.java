package com.dp.spring.parallel.hestia.services;

import com.dp.spring.parallel.hestia.api.dtos.AccessTokenDTO;
import com.dp.spring.parallel.hestia.api.dtos.ExampleRegistrationDTO;
import com.dp.spring.parallel.hestia.api.dtos.LoginDTO;
import com.dp.spring.parallel.hestia.api.dtos.UserDTO;
import com.dp.spring.parallel.hestia.database.entities.User;
import com.dp.spring.parallel.hestia.database.enums.TokenType;
import com.dp.spring.parallel.hestia.database.enums.UserRole;
import com.dp.spring.parallel.hestia.database.repositories.UserRepository;
import com.dp.spring.parallel.hestia.exceptions.EmailNotFoundException;
import com.dp.spring.parallel.hestia.exceptions.WrongCredentialsException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final TokenDetailsService tokenDetailsService;
    private final JWTService jwtService;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;


    public void register(final ExampleRegistrationDTO toRegister) {
        var user = User.builder()
                .firstName( toRegister.getFirstName() )
                .lastName( toRegister.getLastName() )
                .email( toRegister.getEmail() )
                .password( this.passwordEncoder.encode( toRegister.getPassword() ) )
                .role( UserRole.ADMIN )
                .build();
        this.userRepository.save(user);
    }

    /**
     * User authentication via username and password and JWT generation if login successful.
     * @param loginForm the credentials submitted
     * @return the access token
     * @throws AuthenticationException in case of wrong credentials
     */
    public AccessTokenDTO authenticate(final LoginDTO loginForm) throws AuthenticationException {
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
                .orElseThrow( () -> new EmailNotFoundException(loginForm.getEmail()) );

        // Invalidate every current valid token for the authenticated user, and generate a new valid one
        this.tokenDetailsService.revokeAllTokensForUser(user);
        var token = this.jwtService.generateToken(this.extractExtraClaimsFromUser(user), user);
        this.tokenDetailsService.createTokenDetails(token, TokenType.BEARER, user);

        return new AccessTokenDTO(token);
    }


    private Map<String, Object> extractExtraClaimsFromUser(final User user) {
        UserDTO extraClaims = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().getRole())
                .build();

        return this.objectMapper.convertValue(extraClaims, new TypeReference<Map<String, Object>>() {});
    }
}
