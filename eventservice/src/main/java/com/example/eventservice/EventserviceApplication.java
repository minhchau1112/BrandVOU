package com.example.eventservice;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class EventserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventserviceApplication.class, args);
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
