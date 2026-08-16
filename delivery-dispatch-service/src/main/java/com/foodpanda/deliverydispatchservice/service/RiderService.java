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
    private final org.springframework.web.client.RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${service.user.url:http://localhost:8081}")
    private String userServiceUrl;

    public RiderService(RiderRepository riderRepository, org.springframework.web.client.RestTemplate restTemplate) {
        this.riderRepository = riderRepository;
        this.restTemplate = restTemplate;
    }

    public RiderResponse registerRider(RegisterRiderRequest request, String token) {
        // Validate user
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            if (token != null) {
                headers.setBearerAuth(token);
            }
            org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);
            org.springframework.http.ResponseEntity<com.foodpanda.deliverydispatchservice.dto.UserResponse> userRes = restTemplate.exchange(
                    userServiceUrl + "/api/users/me", 
                    org.springframework.http.HttpMethod.GET, 
                    entity, 
                    com.foodpanda.deliverydispatchservice.dto.UserResponse.class
            );
            
            if (!userRes.getStatusCode().is2xxSuccessful() || userRes.getBody() == null) {
                throw new IllegalArgumentException("Invalid user");
            }
            
            com.foodpanda.deliverydispatchservice.dto.UserResponse user = userRes.getBody();
            if (!user.getId().equals(request.getUserId())) {
                throw new IllegalArgumentException("User ID mismatch");
            }
            if (!"RIDER".equals(user.getRole())) {
                throw new IllegalArgumentException("User does not have RIDER role");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("User validation failed: " + e.getMessage());
        }

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
