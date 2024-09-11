package com.example.accountservice.service.impl;

import com.example.accountservice.exception.BrandAlreadyExistException;
import com.example.accountservice.model.account.dto.request.RegisterRequest;
import com.example.accountservice.model.account.dto.request.UpdateBrandRequest;
import com.example.accountservice.model.account.entity.AccountEntity;
import com.example.accountservice.model.account.mapper.UpdateBrandRequestToBrandEntityMapper;
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

import javax.swing.text.html.parser.Entity;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterBrandServiceImpl implements RegisterBrandService {
    private final RegisterServiceImpl registerServiceImpl;
    private final BrandRepository brandRepository;
    private final RegisterBrandRequestToBrandEntityMapper registerBrandRequestToBrandEntityMapper = RegisterBrandRequestToBrandEntityMapper.initialize();
    private final UpdateBrandRequestToBrandEntityMapper updateBrandRequestToBrandEntityMapper = UpdateBrandRequestToBrandEntityMapper.initialize();
    private final AccountRepository accountRepository;
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

    @Override
    public ResponseEntity<?> updateInfoBrand(UpdateBrandRequest updateBrandRequest) {
        Optional<BrandEntity> brandEntity = brandRepository
                .findById(updateBrandRequest.getBrandid());

        if (brandEntity.isPresent()) {
            BrandEntity brandEntityToBeUpdate = brandEntity.get();

            Optional<AccountEntity> accountEntity = accountRepository.findById(brandEntityToBeUpdate.getAccount().getId());

            if (accountEntity.isPresent()) {
                AccountEntity accountEntityToBeUpdate = accountEntity.get();

                updateBrandRequestToBrandEntityMapper.mapForSaving(brandEntityToBeUpdate, updateBrandRequest);

                BrandEntity updatedBrand = brandRepository.save(brandEntityToBeUpdate);

                accountEntityToBeUpdate.setStatus(updateBrandRequest.getStatus());

                AccountEntity updatedAccount = accountRepository.save(accountEntityToBeUpdate);

                if (updatedBrand != null) {
                    return ResponseEntity.ok(updatedBrand);
                }
            }
        }

        return ResponseEntity.ok(null);
    }
}
