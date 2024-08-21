package com.example.brand_backend;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import com.cloudinary.Cloudinary;

@SpringBootApplication
public class BrandBackendApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BrandBackendApplication.class)
                .properties("server.port=9090")
                .run(args);
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary c = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dn8wyfdr8",
                "api_key", "349861636381166",
                "api_secret", "3vZHDfPluF9jGKmnicbndDqf3l8",
                "secure", true
        ));
        return c;
    }
}
