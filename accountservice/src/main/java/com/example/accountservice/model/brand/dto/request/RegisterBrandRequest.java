package com.example.accountservice.model.brand.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterBrandRequest {
    @NotBlank
    @Size(min = 8, message = "Minimum username length is 7 characters.")
    private String username;

    @NotBlank(message = "Password number can't be blank.")
    @Size(min = 8)
    private String password;

    @NotBlank
    @Email(message = "Please enter valid e-mail address")
    @Size(min = 7, message = "Minimum e-mail length is 7 characters.")
    private String email;

    @NotBlank(message = "Phone number can't be blank.")
    @Size(min = 11, max = 20)
    private String phoneNumber;

    @NotBlank(message = "Role can't be blank.")
    private String role;

    @NotBlank(message = "Brand Name can't be blank.")
    private String name;

    @NotBlank(message = "Field can't be blank.")
    private String field;

    @NotBlank(message = "Address can't be blank.")
    private String address;

    @NotNull(message = "GPS_lat can't be blank.")
    private Float gpsLat;

    @NotNull(message = "GPS_long can't be blank.")
    private Float gpsLong;

    @NotBlank(message = "Status of brand can't be blank.")
    private String status;
}
