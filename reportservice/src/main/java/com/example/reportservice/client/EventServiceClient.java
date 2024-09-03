package com.example.reportservice.client;

import com.example.reportservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "eventservice", path = "/api/v1/events", configuration = FeignClientConfig.class)
public interface EventServiceClient {

}
