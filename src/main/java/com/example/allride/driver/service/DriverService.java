package com.example.allride.driver.service;

import com.example.allride.driver.dto.LocationDto;
import com.example.allride.driver.entity.Driver;
import com.example.allride.driver.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    @Transactional
    public void updateLocation(Long driverId, LocationDto dto) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        if (!driver.isOnline()) {
            throw new RuntimeException("Driver is offline, cannot update location");
        }

        driver.setLatitude(dto.getLatitude());
        driver.setLongitude(dto.getLongitude());

        driverRepository.save(driver); // REQUIRED
    }

    @Transactional
    public void setOnline(Long driverId, boolean status) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setOnline(status);

        if (!status) {
            // Optional but smart: clear location when offline
            driver.setLatitude(null);
            driver.setLongitude(null);
        }

        driverRepository.save(driver);
    }
}
