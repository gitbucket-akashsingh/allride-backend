package com.example.allride.ride.controller;

import com.example.allride.auth.authentication.entity.User;
import com.example.allride.ride.service.RideService;
import com.example.allride.ride.dto.RideRequestDto;
import com.example.allride.ride.dto.RideResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/request")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<RideResponseDto> requestRide(@Valid
                                                           @RequestBody RideRequestDto rideRequestDto,
                                                       Authentication authentication) {

        User user=(User) authentication.getPrincipal();
        //     OR
        Long userId = ((User) authentication.getPrincipal()).getId();

         return ResponseEntity.ok(rideService.requestRide(rideRequestDto, user.getId()));

    }


    @GetMapping("/available")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<RideResponseDto>> getAvailableRides() {
        return ResponseEntity.ok(rideService.getAvailableRides());
    }

    @PostMapping("/{rideId}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RideResponseDto> acceptRide(
            @PathVariable Long rideId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                rideService.acceptRide(rideId, user.getId())
        );
    }


    @PostMapping("/{rideId}/start")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RideResponseDto> startRide(
            @PathVariable Long rideId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                rideService.startRide(rideId, user.getId())
        );
    }

    @PostMapping("/{rideId}/complete")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RideResponseDto> completeRide(
            @PathVariable Long rideId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                rideService.completeRide(rideId, user.getId())
        );
    }

    @PostMapping("/{rideId}/cancel")
    public ResponseEntity<RideResponseDto> cancelRide(
            @PathVariable Long rideId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                rideService.cancelRide(rideId, user.getId())
        );
    }

    @GetMapping("/my-rides")
    public ResponseEntity<List<RideResponseDto>> getMyRides(Authentication auth) {

        User user = (User) auth.getPrincipal();

        return ResponseEntity.ok(
                rideService.getMyRides(user.getId(), user.getRole().name())
        );
    }

}
