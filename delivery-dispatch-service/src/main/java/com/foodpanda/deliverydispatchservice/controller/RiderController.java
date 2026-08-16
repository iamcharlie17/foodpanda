package com.foodpanda.deliverydispatchservice.controller;

import com.foodpanda.deliverydispatchservice.dto.RegisterRiderRequest;
import com.foodpanda.deliverydispatchservice.dto.RiderResponse;
import com.foodpanda.deliverydispatchservice.dto.UpdateAvailabilityRequest;
import com.foodpanda.deliverydispatchservice.dto.UpdateLocationRequest;
import com.foodpanda.deliverydispatchservice.service.RiderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery/riders")
public class RiderController {

    private final RiderService riderService;

    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    @PostMapping
    public ResponseEntity<RiderResponse> registerRider(
            @Valid @RequestBody RegisterRiderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(riderService.registerRider(request));
    }

    @PatchMapping("/me/availability")
    public ResponseEntity<RiderResponse> updateAvailability(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody UpdateAvailabilityRequest request) {
        return ResponseEntity.ok(riderService.updateAvailability(userId, request));
    }

    @PatchMapping("/me/location")
    public ResponseEntity<RiderResponse> updateLocation(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody UpdateLocationRequest request) {
        return ResponseEntity.ok(riderService.updateLocation(userId, request));
    }
}
