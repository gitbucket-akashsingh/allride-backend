package com.example.allride.driver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDto {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
