package com.example.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "accountservice", path = "/api/v1/accounts")
public interface UserServiceClient {

    @PostMapping("/validate-token")
    void validateToken(@RequestParam String token);
}
