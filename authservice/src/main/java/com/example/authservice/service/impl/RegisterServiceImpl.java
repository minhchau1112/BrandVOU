package com.example.authservice.service.impl;

import com.example.authservice.client.UserServiceClient;
import com.example.authservice.model.auth.dto.request.RegisterRequest;
import com.example.authservice.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserServiceClient userServiceClient;
    @Override
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        return userServiceClient.register(registerRequest);
    }

}
