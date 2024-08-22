package com.example.brand_backend.controller;

import com.example.brand_backend.model.Accounts;
import com.example.brand_backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Accounts account) {
        return accountService.register(account);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Accounts account) {
        return accountService.login(account);
    }
}