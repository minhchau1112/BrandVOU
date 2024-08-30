package com.example.accountservice.model.account.mapper;

import com.example.accountservice.model.common.mapper.BaseMapper;
import com.example.accountservice.model.account.dto.request.RegisterRequest;
import com.example.accountservice.model.account.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegisterRequestToAccountEntityMapper extends BaseMapper<RegisterRequest, AccountEntity> {

    @Named("mapForSaving")
    default AccountEntity mapForSaving(RegisterRequest userRegisterRequest) {

        return AccountEntity.builder()
                .email(userRegisterRequest.getEmail())
                .username(userRegisterRequest.getUsername())
                .phoneNumber(userRegisterRequest.getPhoneNumber())
                .role(userRegisterRequest.getRole())
                .status(userRegisterRequest.getStatus())
                .build();
    }

    static RegisterRequestToAccountEntityMapper initialize() {
        return Mappers.getMapper(RegisterRequestToAccountEntityMapper.class);
    }

}