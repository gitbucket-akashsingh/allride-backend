package com.example.allride.ride.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RideRequestDto {
    @NotNull
    private Double pickupLatitude;

    @NotNull
    private Double pickupLongitude;

    @NotNull
    private Double dropLatitude;

    @NotNull
    private Double dropLongitude;
}
