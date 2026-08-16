package com.foodpanda.deliverydispatchservice.controller;

import com.foodpanda.deliverydispatchservice.dto.AssignDeliveryRequest;
import com.foodpanda.deliverydispatchservice.dto.DeliveryResponse;
import com.foodpanda.deliverydispatchservice.dto.UpdateDeliveryStatusRequest;
import com.foodpanda.deliverydispatchservice.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<DeliveryResponse> assignDelivery(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody AssignDeliveryRequest request) {
        
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deliveryService.assignDelivery(request, token));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryResponse> updateDeliveryStatus(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UpdateDeliveryStatusRequest request) {
        
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(deliveryService.updateDeliveryStatus(id, request, token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> getDeliveryDetails(@PathVariable String id) {
        return ResponseEntity.ok(deliveryService.getDeliveryDetails(id));
    }
}
