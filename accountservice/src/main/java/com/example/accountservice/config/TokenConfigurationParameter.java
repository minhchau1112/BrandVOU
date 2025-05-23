package com.example.accountservice.config;

import com.example.accountservice.model.account.enums.ConfigurationParameter;
import com.example.accountservice.utils.KeyConverter;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
@Configuration
public class TokenConfigurationParameter {

    private final int accessTokenExpireMinute;
    private final int refreshTokenExpireDay;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public TokenConfigurationParameter() {

        this.accessTokenExpireMinute = Integer.parseInt(
                ConfigurationParameter.AUTH_ACCESS_TOKEN_EXPIRE_MINUTE.getDefaultValue()
        );

        this.refreshTokenExpireDay = Integer.parseInt(
                ConfigurationParameter.AUTH_REFRESH_TOKEN_EXPIRE_DAY.getDefaultValue()
        );

        this.publicKey = KeyConverter.convertPublicKey(
                ConfigurationParameter.AUTH_PUBLIC_KEY.getDefaultValue()
        );

        this.privateKey = KeyConverter.convertPrivateKey(
                ConfigurationParameter.AUTH_PRIVATE_KEY.getDefaultValue()
        );

    }

}
