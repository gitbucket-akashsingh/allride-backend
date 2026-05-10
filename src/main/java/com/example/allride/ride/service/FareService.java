package com.example.allride.ride.service;


import org.springframework.stereotype.Service;

@Service
public class FareService {

    public double calculateFare(double distanceKm) {

        double baseFare = 30;
        double ratePerKm = 10;
        double surgeMultiplier = getSurgeMultiplier();

        return baseFare + (distanceKm * ratePerKm * surgeMultiplier);
    }

    private double getSurgeMultiplier() {
        // simple version
        return 1.0; // later dynamic
    }

}
