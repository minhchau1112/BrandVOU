package com.example.authservice.client;

import com.example.authservice.model.auth.dto.request.LoginRequest;
import com.example.authservice.model.auth.dto.request.RegisterRequest;
import com.example.authservice.model.auth.dto.request.TokenInvalidateRequest;
import com.example.authservice.model.auth.dto.request.TokenRefreshRequest;
import com.example.authservice.model.auth.dto.response.LoginResponse;
import com.example.authservice.model.auth.dto.response.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

// Define a Feign client interface to communicate with the "accountservice" microservice.
@FeignClient(name = "accountservice", path = "/api/v1/accounts")
public interface UserServiceClient {
    // Register a new user by calling the /register endpoint of the user service.
    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody @Valid final RegisterRequest request);
    // Validate the given token by calling the /validate-token endpoint.
    @PostMapping("/validate-token")
    void validateToken(@RequestParam String token);
    // Authenticate a user by calling the /login endpoint.
    // The login credentials are sent in the request body and validated.
    @PostMapping("/login")
    ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid final LoginRequest loginRequest);
    // Refresh the access token by calling the /refresh-token endpoint.
    // The request body contains the necessary data to refresh the token.
    @PostMapping("/refresh-token")
    ResponseEntity<TokenResponse> refreshToken(@RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest);
    // Log the user out by invalidating the token, calling the /logout endpoint.
    @PostMapping("/logout")
    ResponseEntity<Void> logout(@RequestBody @Valid final TokenInvalidateRequest tokenInvalidateRequest);

}
