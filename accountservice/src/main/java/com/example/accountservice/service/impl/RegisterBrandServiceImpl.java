package com.example.accountservice.service.impl;

import com.example.accountservice.exception.BrandAlreadyExistException;
import com.example.accountservice.model.account.dto.request.RegisterRequest;
import com.example.accountservice.model.account.entity.AccountEntity;
import com.example.accountservice.model.brand.dto.request.RegisterBrandRequest;
import com.example.accountservice.model.brand.entity.BrandEntity;
import com.example.accountservice.model.brand.mapper.RegisterBrandRequestToBrandEntityMapper;
import com.example.accountservice.repository.BrandRepository;
import com.example.accountservice.service.RegisterBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterBrandServiceImpl implements RegisterBrandService {
    private final RegisterServiceImpl registerServiceImpl;
    private final BrandRepository brandRepository;
    private final RegisterBrandRequestToBrandEntityMapper registerBrandRequestToBrandEntityMapper = RegisterBrandRequestToBrandEntityMapper.initialize();
    @Override
    public BrandEntity registerBrand(RegisterBrandRequest registerBrandRequest) {
        RegisterRequest registerRequest = new RegisterRequest(
                registerBrandRequest.getUsername(),
                registerBrandRequest.getPassword(),
                registerBrandRequest.getEmail(),
                registerBrandRequest.getPhoneNumber(),
                registerBrandRequest.getRole(),
                registerBrandRequest.getStatus());
        AccountEntity account = registerServiceImpl.registerUser(registerRequest);
        if (account != null) {
            if (brandRepository.existsBrandEntityByName(registerBrandRequest.getName())) {
                throw new BrandAlreadyExistException("The brand name is already used for another brand: " + registerBrandRequest.getName());
            }

            final BrandEntity brandEntityToBeSave = registerBrandRequestToBrandEntityMapper.mapForSaving(registerBrandRequest, account);

            BrandEntity savedBrandEntity = brandRepository.save(brandEntityToBeSave);

            return savedBrandEntity;
        }
        return null;
    }
}
