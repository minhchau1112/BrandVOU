package com.example.accountservice.service;

import com.example.accountservice.model.account.Token;
import com.example.accountservice.model.account.dto.request.LoginRequest;
import com.example.accountservice.model.account.entity.AccountEntity;

public interface UserLoginService {

    Token login(final LoginRequest loginRequest);
    AccountEntity loadUserByUsername(String username);
}
