package com.foodpanda.deliverydispatchservice.service;

import com.foodpanda.deliverydispatchservice.dto.RegisterRiderRequest;
import com.foodpanda.deliverydispatchservice.dto.RiderResponse;
import com.foodpanda.deliverydispatchservice.dto.UpdateAvailabilityRequest;
import com.foodpanda.deliverydispatchservice.dto.UpdateLocationRequest;
import com.foodpanda.deliverydispatchservice.exception.RiderNotFoundException;
import com.foodpanda.deliverydispatchservice.model.Location;
import com.foodpanda.deliverydispatchservice.model.Rider;
import com.foodpanda.deliverydispatchservice.repository.RiderRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    public RiderResponse registerRider(RegisterRiderRequest request) {
        Rider rider = new Rider();
        rider.setUserId(request.getUserId());
        rider.setVehicleType(request.getVehicleType());
        rider.setIsAvailable(false);
        rider.setCurrentLocation(null);

        Rider saved = riderRepository.save(rider);
        return mapToResponse(saved);
    }

    public RiderResponse updateAvailability(String userId, UpdateAvailabilityRequest request) {
        Rider rider = riderRepository.findByUserId(userId)
                .orElseThrow(() -> new RiderNotFoundException("userId: " + userId));

        rider.setIsAvailable(request.getIsAvailable());
        Rider saved = riderRepository.save(rider);
        
        return mapToResponse(saved);
    }

    public RiderResponse updateLocation(String userId, UpdateLocationRequest request) {
        Rider rider = riderRepository.findByUserId(userId)
                .orElseThrow(() -> new RiderNotFoundException("userId: " + userId));

        rider.setCurrentLocation(new Location(request.getLat(), request.getLng(), Instant.now()));
        Rider saved = riderRepository.save(rider);

        return mapToResponse(saved);
    }

    private RiderResponse mapToResponse(Rider rider) {
        RiderResponse response = new RiderResponse();
        response.setId(rider.getId());
        response.setUserId(rider.getUserId());
        response.setVehicleType(rider.getVehicleType());
        response.setIsAvailable(rider.getIsAvailable());
        response.setCurrentLocation(rider.getCurrentLocation());
        return response;
    }
}
