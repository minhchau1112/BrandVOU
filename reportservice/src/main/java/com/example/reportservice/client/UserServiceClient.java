package com.example.reportservice.client;

import com.example.reportservice.config.FeignClientConfig;
import com.example.reportservice.model.entity.BrandEntity;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "accountservice", path = "/api/v1/accounts", configuration = FeignClientConfig.class)
public interface UserServiceClient {

    @PostMapping("/validate-token")
    void validateToken(@RequestParam String token);

    @GetMapping("/authenticate")
    UsernamePasswordAuthenticationToken getAuthentication(@RequestParam String token);

    @GetMapping("/brands")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BRAND')")
    BrandEntity findBrandById(@RequestParam Long brandId,  @RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
