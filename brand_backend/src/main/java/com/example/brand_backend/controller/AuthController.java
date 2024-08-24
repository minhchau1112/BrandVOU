package com.example.brand_backend.controller;

import com.example.brand_backend.model.Accounts;
import com.example.brand_backend.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Accounts account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setCreatedAt(Timestamp.from(Instant.now()));
        account.setUpdatedAt(Timestamp.from(Instant.now()));
        accountsRepository.save(account);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Accounts account) {
        Accounts existingAccount = accountsRepository.findByUsername(account.getUsername());
        if (existingAccount != null && passwordEncoder.matches(account.getPassword(), existingAccount.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", existingAccount.getId());
            response.put("username", existingAccount.getUsername());

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }
}
