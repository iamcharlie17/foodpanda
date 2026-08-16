package com.foodpanda.deliverydispatchservice.service;

import com.foodpanda.deliverydispatchservice.dto.AssignDeliveryRequest;
import com.foodpanda.deliverydispatchservice.dto.DeliveryResponse;
import com.foodpanda.deliverydispatchservice.dto.UpdateDeliveryStatusRequest;
import com.foodpanda.deliverydispatchservice.exception.DeliveryNotFoundException;
import com.foodpanda.deliverydispatchservice.exception.InvalidDeliveryStatusTransitionException;
import com.foodpanda.deliverydispatchservice.exception.NoRiderAvailableException;
import com.foodpanda.deliverydispatchservice.exception.RiderNotFoundException;
import com.foodpanda.deliverydispatchservice.model.Delivery;
import com.foodpanda.deliverydispatchservice.model.DeliveryStatus;
import com.foodpanda.deliverydispatchservice.model.Rider;
import com.foodpanda.deliverydispatchservice.model.RoutePoint;
import com.foodpanda.deliverydispatchservice.repository.DeliveryRepository;
import com.foodpanda.deliverydispatchservice.repository.RiderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

@Service
public class DeliveryService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

    private final DeliveryRepository deliveryRepository;
    private final RiderRepository riderRepository;
    private final RestTemplate restTemplate;

    @Value("${service.order.url}")
    private String orderServiceUrl;

    @Value("${service.notification.url}")
    private String notificationServiceUrl;

    public DeliveryService(DeliveryRepository deliveryRepository, RiderRepository riderRepository, RestTemplate restTemplate) {
        this.deliveryRepository = deliveryRepository;
        this.riderRepository = riderRepository;
        this.restTemplate = restTemplate;
    }

    public DeliveryResponse assignDelivery(AssignDeliveryRequest request, String token) {
        // Find rider: If riderId is provided, use it; otherwise, find any available rider
        Rider rider;
        if (request.getRiderId() != null && !request.getRiderId().isBlank()) {
            rider = riderRepository.findById(request.getRiderId())
                    .orElseThrow(() -> new RiderNotFoundException(request.getRiderId()));
            if (!rider.getIsAvailable()) {
                throw new NoRiderAvailableException();
            }
        } else {
            rider = riderRepository.findFirstByIsAvailableTrue()
                    .orElseThrow(NoRiderAvailableException::new);
        }

        // Create delivery
        Delivery delivery = new Delivery();
        delivery.setOrderId(request.getOrderId());
        delivery.setRiderId(rider.getId());
        delivery.setStatus(DeliveryStatus.ASSIGNED.name());
        delivery.setAssignedAt(Instant.now());
        delivery.setRoute(new ArrayList<>());

        Delivery saved = deliveryRepository.save(delivery);
        
        // Update rider availability (they are now busy)
        rider.setIsAvailable(false);
        riderRepository.save(rider);

        // Synchronous call to Notification Service to alert customer
        notifyCustomer(request.getOrderId(), "A rider has been assigned to your order.", token);

        return mapToResponse(saved);
    }

    public DeliveryResponse updateDeliveryStatus(String deliveryId, UpdateDeliveryStatusRequest request, String token) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));

        DeliveryStatus currentStatus = DeliveryStatus.valueOf(delivery.getStatus());
        DeliveryStatus newStatus = DeliveryStatus.valueOf(request.getStatus());

        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new InvalidDeliveryStatusTransitionException(currentStatus.name(), newStatus.name());
        }

        delivery.setStatus(newStatus.name());
        
        if (request.getLocation() != null) {
            delivery.getRoute().add(new RoutePoint(
                    request.getLocation().getLat(),
                    request.getLocation().getLng(),
                    Instant.now()
            ));
        }

        if (newStatus == DeliveryStatus.DELIVERED) {
            delivery.setDeliveredAt(Instant.now());
            // Sync call to order-service to mark order as delivered
            updateOrderServiceStatus(delivery.getOrderId(), newStatus.name(), token);
            // Sync call to notification-service
            notifyCustomer(delivery.getOrderId(), "Your order has been delivered!", token);
        }

        Delivery saved = deliveryRepository.save(delivery);
        return mapToResponse(saved);
    }

    public DeliveryResponse getDeliveryDetails(String deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
        return mapToResponse(delivery);
    }

    // ── Inter-Service Calls ──────────────────────────────────────────────────

    private void updateOrderServiceStatus(String orderId, String status, String token) {
        try {
            String url = orderServiceUrl + "/api/orders/" + orderId + "/status";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            
            Map<String, String> body = Map.of("status", status);
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

            restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, String.class);
            log.info("Successfully updated order {} status to {}", orderId, status);
        } catch (Exception e) {
            log.error("Failed to update order status in order-service", e);
        }
    }

    private void notifyCustomer(String orderId, String message, String token) {
        try {
            // Assuming an internal/system notification endpoint
            String url = notificationServiceUrl + "/api/notifications/system";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            Map<String, String> body = Map.of(
                    "orderId", orderId,
                    "message", message
            );
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("Successfully sent notification for order {}", orderId);
        } catch (Exception e) {
            log.error("Failed to send notification via notification-service", e);
        }
    }

    // ── Mappers ─────────────────────────────────────────────────────────────

    private DeliveryResponse mapToResponse(Delivery delivery) {
        return DeliveryResponse.builder()
                .id(delivery.getId())
                .orderId(delivery.getOrderId())
                .riderId(delivery.getRiderId())
                .status(delivery.getStatus())
                .route(delivery.getRoute())
                .assignedAt(delivery.getAssignedAt())
                .deliveredAt(delivery.getDeliveredAt())
                .build();
    }
}
