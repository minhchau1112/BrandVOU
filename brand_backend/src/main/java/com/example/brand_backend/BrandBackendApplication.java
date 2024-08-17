package com.example.brand_backend;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BrandBackendApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BrandBackendApplication.class)
                .properties("server.port=9090")
                .run(args);
    }

}
