package com.example.authservice.service.impl;

import com.example.authservice.client.UserServiceClient;
import com.example.authservice.model.auth.dto.request.TokenInvalidateRequest;
import com.example.authservice.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {
    private final UserServiceClient userServiceClient;
    @Override
    public ResponseEntity<Void> logout(TokenInvalidateRequest tokenInvalidateRequest) {
        return userServiceClient.logout(tokenInvalidateRequest);
    }

}
