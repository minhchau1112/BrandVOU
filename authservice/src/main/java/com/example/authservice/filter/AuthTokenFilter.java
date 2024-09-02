package com.example.authservice.filter;

import com.example.authservice.client.UserServiceClient;
import com.example.authservice.model.auth.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j // Enables logging for this class
@Component // Registers this filter as a Spring Bean
@RequiredArgsConstructor // Automatically generates constructor for final fields
public class AuthTokenFilter extends OncePerRequestFilter {
    // Feign client for interacting with userService to validate tokens
    private final UserServiceClient userServiceClient;
    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest httpServletRequest,
                                    @NonNull final HttpServletResponse httpServletResponse,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {

        log.debug("API Request was secured with Security!");
        // Get the authorization header from the request
        // Lấy header xác thực từ yêu cầu
        final String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        // Check if the header contains a Bearer token and validate it
        if (Token.isBearerToken(authorizationHeader)) {
            final String jwt = Token.getJwt(authorizationHeader);
            userServiceClient.validateToken(jwt);
        }
        // Continue with the request processing
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

}
