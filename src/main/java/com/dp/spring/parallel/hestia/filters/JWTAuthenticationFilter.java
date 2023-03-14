package com.dp.spring.parallel.hestia.filters;

import com.dp.spring.parallel.hestia.database.entities.TokenDetails;
import com.dp.spring.parallel.hestia.database.repositories.TokenDetailsRepository;
import com.dp.spring.parallel.hestia.exceptions.TokenNotValidException;
import com.dp.spring.parallel.hestia.services.JWTService;
import com.dp.spring.springcore.v2.model.error.ErrorModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.dp.spring.parallel.hestia.services.LogoutService.LOGOUT_URL;
import static com.dp.spring.parallel.hestia.utils.JWTUtils.AUTHORIZATION_HEADER_NAME;
import static com.dp.spring.parallel.hestia.utils.JWTUtils.BEARER_PREFIX;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenDetailsRepository tokenDetailsRepository;


    /**
     * Applying authentication through JWT.
     * <br>
     * In case of {@link TokenNotValidException}, exception resolved generating an
     * {@link ErrorModel} as response.
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain of responsibility
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if ( SecurityContextHolder.getContext().getAuthentication() != null  ||
                request.getRequestURI().startsWith(LOGOUT_URL) ) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
        final String token;

        // Case: no Authorization header or no JWT in Authorization header:
        if ( authHeader == null  ||  !authHeader.startsWith(BEARER_PREFIX) ) {
            filterChain.doFilter(request, response);
            return;
        }

        // Case: JWT in Authorization header
        token = authHeader.substring(BEARER_PREFIX.length());

        this.validateJWT(token);
        this.setSecurityContextHolderAuthentication(token, request);

        filterChain.doFilter(request, response);
    }

    /**
     * Internal function that validates a JWT, based on both its content and the corresponding
     * {@link TokenDetails} in the database.
     * @param token the JWT
     */
    private void validateJWT(final String token) {
        // TokenNotValid if it doesn't exist in db
        var isTokenRevoked = this.tokenDetailsRepository.findByToken(token)
                .map(TokenDetails::isRevoked)
                .orElseThrow(TokenNotValidException::new);

        // TokenNotValid if it's been revoked  or  no subject is set or if expiring date passed
        if ( isTokenRevoked || !this.jwtService.isTokenValid(token) ) {
            throw new TokenNotValidException();
        }
    }

    /**
     * Internal function that deduces {@link UserDetails} and {@link GrantedAuthority}s from the token subject,
     * in order to save the just completed {@link Authentication} in the {@link SecurityContextHolder}.
     * @param token the JWT
     * @param request the HTTP request
     */
    private void setSecurityContextHolderAuthentication(final String token, final HttpServletRequest request) {
        final String username = this.jwtService.extractUsername(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request) );

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
