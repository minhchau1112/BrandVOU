package com.example.authservice.service;

import com.example.authservice.model.auth.dto.request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface RegisterService {
    ResponseEntity<?> registerUser(final RegisterRequest registerRequest);
}
