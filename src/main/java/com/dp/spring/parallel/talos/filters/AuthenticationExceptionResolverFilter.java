package com.dp.spring.parallel.talos.filters;

import com.dp.spring.parallel.common.exceptions.EmailNotFoundException;
import com.dp.spring.parallel.common.exceptions.TokenNotValidException;
import com.dp.spring.springcore.handlers.strategies.PreserveErrorsInformationHandlingStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationExceptionResolverFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;


    /**
     * Trying JWT authentication and resolving exceptions thrown.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain of responsibility
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenNotValidException e) {
            e.printStackTrace();
            this.handleTokenNotValidException(request, response, e);
        } catch (EmailNotFoundException e) {
            e.printStackTrace();
            this.handleEmailNotFoundException(request, response, e);
        }
    }

    /**
     * {@link TokenNotValidException} resolution, generating an appropriate response.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param e        the exception thrown
     */
    private void handleTokenNotValidException(final HttpServletRequest request,
                                              final HttpServletResponse response,
                                              final TokenNotValidException e) throws IOException {
        var errorModel = PreserveErrorsInformationHandlingStrategy.getInstance()
                .handle(e, e.getStatus())
                .getBody();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorModel));
    }

    /**
     * {@link EmailNotFoundException} resolution, generating an appropriate response.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param e        the exception thrown
     */
    private void handleEmailNotFoundException(final HttpServletRequest request,
                                              final HttpServletResponse response,
                                              final EmailNotFoundException e) throws IOException {
        var errorModel = PreserveErrorsInformationHandlingStrategy.getInstance()
                .handle(e, e.getStatus())
                .getBody();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorModel));
    }
}
