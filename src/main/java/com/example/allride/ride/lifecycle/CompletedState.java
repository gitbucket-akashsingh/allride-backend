package com.example.allride.ride.lifecycle;

import com.example.allride.ride.entity.Ride;

public class CompletedState implements RideState{

    @Override
    public void startRide(Ride ride) {
        throw new RuntimeException("Ride already completed");
    }

    @Override
    public void completeRide(Ride ride) {
        throw new RuntimeException("Ride already completed");
    }

    @Override
    public void cancelRide(Ride ride) {
        throw new RuntimeException("Completed ride cannot be cancelled");
    }


}
