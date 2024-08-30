package com.example.authservice.service;

import com.example.authservice.model.auth.Account;
import com.example.authservice.model.auth.dto.request.RegisterRequest;

public interface RegisterService {
    Account registerUser(final RegisterRequest registerRequest);
}
