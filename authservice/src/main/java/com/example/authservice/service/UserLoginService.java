package com.example.authservice.service;

import com.example.authservice.model.auth.dto.request.LoginRequest;
import com.example.authservice.model.auth.dto.response.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface UserLoginService {
    ResponseEntity<LoginResponse> login(final LoginRequest loginRequest);
}
