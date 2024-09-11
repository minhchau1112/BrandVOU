package com.example.accountservice.model.account.dto.request;

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
public class UpdateBrandRequest {
    @NotNull
    private Long brandid;

    @NotBlank(message = "Status can't be blank.")
    private String status;

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
}
