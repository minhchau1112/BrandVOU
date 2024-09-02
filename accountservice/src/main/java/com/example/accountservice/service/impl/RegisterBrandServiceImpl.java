package com.example.accountservice.service.impl;

import com.example.accountservice.exception.BrandAlreadyExistException;
import com.example.accountservice.model.account.dto.request.RegisterRequest;
import com.example.accountservice.model.account.entity.AccountEntity;
import com.example.accountservice.model.brand.dto.request.RegisterBrandRequest;
import com.example.accountservice.model.brand.entity.BrandEntity;
import com.example.accountservice.model.brand.mapper.RegisterBrandRequestToBrandEntityMapper;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.repository.BrandRepository;
import com.example.accountservice.service.RegisterBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterBrandServiceImpl implements RegisterBrandService {
    private final RegisterServiceImpl registerServiceImpl;
    private final BrandRepository brandRepository;
    private final RegisterBrandRequestToBrandEntityMapper registerBrandRequestToBrandEntityMapper = RegisterBrandRequestToBrandEntityMapper.initialize();
    @Override
    public ResponseEntity<?> registerBrand(RegisterBrandRequest registerBrandRequest) {
        RegisterRequest registerRequest = new RegisterRequest(
                registerBrandRequest.getUsername(),
                registerBrandRequest.getPassword(),
                registerBrandRequest.getEmail(),
                registerBrandRequest.getPhoneNumber(),
                registerBrandRequest.getRole(),
                registerBrandRequest.getStatus());

        if (brandRepository.existsBrandEntityByName(registerBrandRequest.getName())) {
            return ResponseEntity.ok(new BrandAlreadyExistException("The brand name is already used for another brand"));
        }

        ResponseEntity<?> accountResponse = registerServiceImpl.registerUser(registerRequest);
        log.info("accountResponse: " + accountResponse);

        int status = accountResponse.getStatusCode().value();
        Object body = accountResponse.getBody();

        if (status == HttpStatus.CREATED.value()) {
            AccountEntity account = (AccountEntity) body;
            final BrandEntity brandEntityToBeSave = registerBrandRequestToBrandEntityMapper.mapForSaving(registerBrandRequest, account);

            BrandEntity savedBrandEntity = brandRepository.save(brandEntityToBeSave);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedBrandEntity);
        }

        return ResponseEntity.ok(body);
    }
}
