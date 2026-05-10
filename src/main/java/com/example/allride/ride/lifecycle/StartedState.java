package com.example.allride.ride.lifecycle;

import com.example.allride.ride.entity.Ride;
import com.example.allride.ride.enums.RideStatus;

import java.time.LocalDateTime;

public class StartedState implements RideState{

    @Override
    public void startRide(Ride ride) {
        throw new RuntimeException("Ride already started");
    }

    @Override
    public void completeRide(Ride ride) {
        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());
    }

    @Override
    public void cancelRide(Ride ride) {
        ride.setStatus(RideStatus.CANCELLED);
    }
}
