package com.example.allride.ride.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RideResponseDto {

    private Long rideId;
//    private String pickupLocation;
//    private String dropLocation;
//    private Double fare;
    private String status;
    private String message;
}
