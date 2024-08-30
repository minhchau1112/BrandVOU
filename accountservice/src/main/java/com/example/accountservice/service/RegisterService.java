package com.example.accountservice.service;

import com.example.accountservice.model.account.dto.request.RegisterRequest;
import com.example.accountservice.model.account.entity.AccountEntity;

public interface RegisterService {

    AccountEntity registerUser(final RegisterRequest registerRequest);

}
