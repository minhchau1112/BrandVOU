package com.example.brand_backend.controller;

import com.example.brand_backend.model.Accounts;
import com.example.brand_backend.model.Brands;
import com.example.brand_backend.repository.AccountsRepository;
import com.example.brand_backend.repository.BrandRepository;
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
    private BrandRepository brandRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> payload) {
        // Extract account information
        Accounts account = new Accounts();
        account.setUsername((String) payload.get("username"));
        account.setPassword(passwordEncoder.encode((String) payload.get("password")));
        account.setEmail((String) payload.get("email"));
        account.setPhoneNumber((String) payload.get("phoneNumber"));
        account.setCreatedAt(Timestamp.from(Instant.now()));
        account.setUpdatedAt(Timestamp.from(Instant.now()));
        Accounts savedAccount = accountsRepository.save(account);

        // Create a new brand entry
        Brands brand = new Brands();
        brand.setAccount(savedAccount);
        brand.setName((String) payload.get("name"));
        brand.setField((String) payload.get("field"));
        brand.setAddress((String) payload.get("address"));
        // Set default values for GPS coordinates and status
        brand.setGPS_lat(0.0f);
        brand.setGPS_long(0.0f);
        brand.setStatus("Available");
        brandRepository.save(brand);

        return ResponseEntity.ok("User registered successfully with brand information");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Accounts account) {
        Accounts existingAccount = accountsRepository.findByUsername(account.getUsername());
        if (existingAccount != null && passwordEncoder.matches(account.getPassword(), existingAccount.getPassword())) {
            Brands brand = brandRepository.findByAccountId(existingAccount.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("id", existingAccount.getId());
            response.put("username", existingAccount.getUsername());
            response.put("brandName", brand != null ? brand.getName() : null); // Include brand name in the response
            response.put("brandId", brand != null ? brand.getId() : null); // Include brand ID in the response

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }

}
