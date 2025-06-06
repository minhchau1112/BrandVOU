package com.example.accountservice.service;

import com.example.accountservice.model.account.Token;
import com.example.accountservice.model.account.dto.request.TokenRefreshRequest;

public interface RefreshTokenService {
    Token refreshToken(final TokenRefreshRequest tokenRefreshRequest);
}
