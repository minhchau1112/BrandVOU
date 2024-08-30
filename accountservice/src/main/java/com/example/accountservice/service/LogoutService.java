package com.example.accountservice.service;

import com.example.accountservice.model.account.dto.request.TokenInvalidateRequest;

public interface LogoutService {

    void logout(final TokenInvalidateRequest tokenInvalidateRequest);

}
