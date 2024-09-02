package com.example.authservice.service;

import com.example.authservice.model.auth.dto.request.TokenRefreshRequest;
import com.example.authservice.model.auth.dto.response.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface RefreshTokenService {
    ResponseEntity<TokenResponse> refreshToken(@RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest);
}
