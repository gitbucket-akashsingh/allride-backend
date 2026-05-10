package com.example.allride.ride.lifecycle;

import com.example.allride.ride.entity.Ride;
import com.example.allride.ride.enums.RideStatus;

import java.time.LocalDateTime;

public class AcceptedState implements RideState{
    @Override
    public void startRide(Ride ride) {
        ride.setStatus(RideStatus.STARTED);
        ride.setStartedAt(LocalDateTime.now());
    }

    @Override
    public void completeRide(Ride ride) {

        throw new RuntimeException("Start ride before completing");
    }

    @Override
    public void cancelRide(Ride ride) {

        ride.setStatus(RideStatus.CANCELLED);
    }
}
