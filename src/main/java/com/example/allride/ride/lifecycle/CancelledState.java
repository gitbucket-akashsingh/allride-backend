package com.example.allride.ride.lifecycle;

import com.example.allride.ride.entity.Ride;

public class CancelledState implements RideState{

    @Override
    public void startRide(Ride ride) {
        throw new RuntimeException("Cancelled ride cannot start");
    }

    @Override
    public void completeRide(Ride ride) {
        throw new RuntimeException("Cancelled ride cannot complete");
    }

    @Override
    public void cancelRide(Ride ride) {
        throw new RuntimeException("Already cancelled");
    }
}

