package com.example.accountservice.model.account.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank
    @Size(min = 8, message = "Minimum username length is 7 characters.")
    private String username;

    @NotBlank(message = "Phone number can't be blank.")
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

    @NotBlank(message = "Status can't be blank.")
    private String status;
}
