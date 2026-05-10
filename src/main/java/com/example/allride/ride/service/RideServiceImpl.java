package com.example.allride.ride.service;

import com.example.allride.ride.enums.RideStatus;
import com.example.allride.ride.dto.RideRequestDto;
import com.example.allride.ride.dto.RideResponseDto;
import com.example.allride.ride.entity.Ride;
import com.example.allride.ride.lifecycle.RideState;
import com.example.allride.ride.lifecycle.RideStateFactory;
import com.example.allride.ride.mapper.RideMapper;
import com.example.allride.ride.repository.RideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final RideStateFactory stateFactory;
    private final FareService fareService;
    private final DistanceService distanceService;

    // REQUEST RIDE API -- USED BY RIDER
    @Override
    public RideResponseDto requestRide(RideRequestDto rideRequestDto, Long passengerId) {

//        double fare = fareService.calculateFare(rideRequestDto.getDistanceKm());
        double distance = distanceService.calculateDistance(
                rideRequestDto.getPickupLatitude(),
                rideRequestDto.getPickupLongitude(),
                rideRequestDto.getDropLatitude(),
                rideRequestDto.getDropLongitude()
        );
        double fare = fareService.calculateFare(distance);


        Ride ride = rideMapper.toEntity(rideRequestDto, passengerId);

        Ride saveRide = rideRepository.save(ride);
        return rideMapper.toResponse(saveRide);

    }

    // ACCEPT RIDE API -- USED BY DRIVER
    @Transactional
    @Override
    public RideResponseDto acceptRide(Long rideId, Long driverId) {

        Ride ride = rideRepository.findByIdForUpdate(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new RuntimeException("Ride already accepted or not available");
        }

        ride.setStatus(RideStatus.ACCEPTED);
        ride.setDriverId(driverId);

        Ride saved = rideRepository.save(ride);

        return rideMapper.mapToDto(saved);
    }

    // CHECK REQUESTED AVAILABLE RIDE API -- USED BY DRIVER
    @Override
    public List<RideResponseDto> getAvailableRides() {

        return rideRepository.findByStatus(RideStatus.REQUESTED)
                .stream()
                .map(rideMapper::mapToDto)
                .toList();
    }


    // START RIDE API --USED BY DRIVER
    @Transactional
    @Override
    public RideResponseDto startRide(Long rideId, Long driverId) {

        Ride ride = rideRepository.findByIdForUpdate(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new RuntimeException("Ride must be ACCEPTED to start");
        }

        if (!ride.getDriverId().equals(driverId)) {
            throw new RuntimeException("Unauthorized driver");
        }

        RideState state = stateFactory.getState(ride.getStatus());
        state.startRide(ride);

        ride.setStatus(RideStatus.STARTED);
        ride.setStartedAt(LocalDateTime.now());

        return rideMapper.mapToDto(ride);
    }


    // COMPLETE RIDE API -- USED BY DRIVER
    @Transactional
    @Override
    public RideResponseDto completeRide(Long rideId, Long driverId) {

        Ride ride = rideRepository.findByIdForUpdate(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getStatus() != RideStatus.STARTED) {
            throw new RuntimeException("Ride must be STARTED to complete");
        }

        if (!ride.getDriverId().equals(driverId)) {
            throw new RuntimeException("Unauthorized driver");
        }

        RideState state = stateFactory.getState(ride.getStatus());
        state.completeRide(ride);

        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());

        return rideMapper.mapToDto(ride);
    }


    // CANCEL RIDE API -- USED BY BOTH DRIVER or RIDER
    @Transactional
    @Override
    public RideResponseDto cancelRide(Long rideId, Long userId) {

        Ride ride = rideRepository.findByIdForUpdate(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new RuntimeException("Completed ride cannot be cancelled");
        }

        // Passenger or assigned driver can cancel
        boolean isPassenger = ride.getPassengerId().equals(userId);
        boolean isDriver = ride.getDriverId() != null && ride.getDriverId().equals(userId);

        if (!isPassenger && !isDriver) {
            throw new RuntimeException("Not authorized to cancel this ride");
        }

        RideState state = stateFactory.getState(ride.getStatus());
        state.cancelRide(ride);

        ride.setStatus(RideStatus.CANCELLED);

        return rideMapper.mapToDto(ride);
    }

    public List<RideResponseDto> getMyRides(Long userId, String role) {

        List<Ride> rides;

        if (role.equals("PASSENGER")) {
            rides = rideRepository.findByPassengerId(userId);
        } else {
            rides = rideRepository.findByDriverId(userId);
        }

        return rides.stream()
                .map(rideMapper::mapToDto)
                .toList();
    }
}
