package com.example.allride.ride.service;

import org.springframework.stereotype.Service;

@Service
public class DistanceServiceImpl implements DistanceService{

    @Override
    public double calculateDistance(double pickupLat, double pickupLng, double destinationLat, double destinationLng) {

        // Earth's radius in KM
        final double EARTH_RADIUS = 6371;

        double latDistance = Math.toRadians(destinationLat - pickupLat);
        double lngDistance = Math.toRadians(destinationLng - pickupLng);

        double a =
                Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                        + Math.cos(Math.toRadians(pickupLat))
                        * Math.cos(Math.toRadians(destinationLat))
                        * Math.sin(lngDistance / 2)
                        * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;

    }
}
