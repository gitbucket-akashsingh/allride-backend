package com.example.allride.ride.service;

import com.example.allride.ride.dto.RideRequestDto;
import com.example.allride.ride.dto.RideResponseDto;

import java.util.List;

public interface RideService {

    RideResponseDto requestRide(RideRequestDto dto, Long passengerId);

    List<RideResponseDto> getAvailableRides();

    RideResponseDto acceptRide(Long rideId, Long driverId);

    RideResponseDto startRide(Long rideId, Long driverId);

    RideResponseDto completeRide(Long rideId, Long driverId);

    RideResponseDto cancelRide(Long rideId, Long userId);

    List<RideResponseDto> getMyRides(Long id, String name);
}
