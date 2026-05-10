package com.example.allride.ride.lifecycle;

import com.example.allride.ride.enums.RideStatus;
import org.springframework.stereotype.Component;

@Component
public class RideStateFactory {

    public RideState getState(RideStatus status) {
        return switch (status) {
            case REQUESTED -> new RequestedState();
            case ACCEPTED -> new AcceptedState();
            case STARTED -> new StartedState();
            case COMPLETED -> new CompletedState();
            case CANCELLED -> new CancelledState();
            case PAID -> null;
        };
    }
}
