package com.example.allride.ride.lifecycle;

import com.example.allride.ride.entity.Ride;

public interface RideState {

    void startRide(Ride ride);

    void completeRide(Ride ride);

    void cancelRide(Ride ride);
}
