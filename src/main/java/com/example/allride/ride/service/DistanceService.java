package com.example.allride.ride.service;

public interface DistanceService {

    double calculateDistance(
            double pickupLat,
            double pickupLng,
            double destinationLat,
            double destinationLng
    );
}
