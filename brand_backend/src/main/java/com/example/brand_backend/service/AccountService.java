package com.example.brand_backend.service;

import com.example.brand_backend.model.Accounts;
import com.example.brand_backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<String> register(Accounts account) {
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        accountRepository.save(account);
        return ResponseEntity.ok("Registration successful");
    }

    public ResponseEntity<String> login(Accounts account) {
        Accounts existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount == null || !existingAccount.getPassword().equals(account.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
        return ResponseEntity.ok("Login successful");
    }
}