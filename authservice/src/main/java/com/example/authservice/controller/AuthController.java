package com.example.authservice.controller;

import com.example.authservice.model.auth.dto.request.LoginRequest;
import com.example.authservice.model.auth.dto.request.RegisterRequest;
import com.example.authservice.model.auth.dto.request.TokenInvalidateRequest;
import com.example.authservice.model.auth.dto.request.TokenRefreshRequest;
import com.example.authservice.model.auth.dto.response.LoginResponse;
import com.example.authservice.model.auth.dto.response.TokenResponse;
import com.example.authservice.service.LogoutService;
import com.example.authservice.service.RefreshTokenService;
import com.example.authservice.service.RegisterService;
import com.example.authservice.service.UserLoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/authentication/accounts")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterService registerService;

    private final UserLoginService userLoginService;

    private final RefreshTokenService refreshTokenService;

    private final LogoutService logoutService;

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid final RegisterRequest registerRequest) {
        ResponseEntity<?> account = registerService.registerUser(registerRequest);
        return account;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid final LoginRequest loginRequest) {
        log.info("AuthController | loginUser | username = " + loginRequest.getUsername() + " | password = " + loginRequest.getPassword());
        return userLoginService.login(loginRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest) {
        return refreshTokenService.refreshToken(tokenRefreshRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid final TokenInvalidateRequest tokenInvalidateRequest) {
        logoutService.logout(tokenInvalidateRequest);
        return ResponseEntity.ok().build();
    }

}
