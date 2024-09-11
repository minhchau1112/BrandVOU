package com.example.authservice.service.impl;

import com.example.authservice.client.UserServiceClient;
import com.example.authservice.model.auth.dto.request.LoginRequest;
import com.example.authservice.model.auth.dto.response.LoginResponse;
import com.example.authservice.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {
    private final UserServiceClient userServiceClient;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        return userServiceClient.loginUser(loginRequest);
    }
}