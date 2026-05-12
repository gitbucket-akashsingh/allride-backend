package com.example.allride.driver.controller;

import com.example.allride.driver.service.DriverService;
import com.example.allride.driver.dto.LocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
//    private final User user;

//    @PostMapping("/driver/online")
//    public void goOnline(@AuthenticationPrincipal CustomUserDetails user) {
////        Long driverId = getUserId(auth);
//        driverService.setOnline(user.getId(), true);
//    }

@PostMapping("/online")
    public void goOnline(Authentication auth) {
        Long driverId = Long.parseLong(auth.getName());
        driverService.setOnline(driverId, true);
    }

    @PostMapping("offline")
    public void goOffline(Authentication auth) {
//        Long driverId = getUserId(auth);
        Long driverId = Long.parseLong(auth.getName());
        driverService.setOnline(driverId, false);
    }

    @PostMapping("location")
    public void updateLocation(@RequestBody LocationDto dto, Authentication auth) {
//        Long driverId = getUserId(auth);
        Long driverId = Long.parseLong(auth.getName());
        driverService.updateLocation(driverId, dto);
    }
}
