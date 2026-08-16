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
    private final org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    @Value("${service.order.url}")
    private String orderServiceUrl;

    public DeliveryService(DeliveryRepository deliveryRepository, RiderRepository riderRepository, RestTemplate restTemplate, org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate) {
        this.deliveryRepository = deliveryRepository;
        this.riderRepository = riderRepository;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public DeliveryResponse assignDelivery(AssignDeliveryRequest request, String token) {
        // Validate order
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            if (token != null) {
                headers.setBearerAuth(token);
            }
            org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);
            org.springframework.http.ResponseEntity<com.foodpanda.deliverydispatchservice.dto.OrderResponse> orderRes = restTemplate.exchange(
                    orderServiceUrl + "/api/orders/" + request.getOrderId(), 
                    org.springframework.http.HttpMethod.GET, 
                    entity, 
                    com.foodpanda.deliverydispatchservice.dto.OrderResponse.class
            );
            
            if (!orderRes.getStatusCode().is2xxSuccessful() || orderRes.getBody() == null) {
                throw new IllegalArgumentException("Invalid order");
            }
            
            com.foodpanda.deliverydispatchservice.dto.OrderResponse order = orderRes.getBody();
            if (!"READY".equals(order.getStatus())) {
                throw new IllegalArgumentException("Order is not READY for delivery");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Order validation failed: " + e.getMessage());
        }

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

        // Publish event (instead of synchronous call to notification/order service)
        publishDeliveryEvent(saved);

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
        }

        Delivery saved = deliveryRepository.save(delivery);
        
        // Publish event for order/notification service to consume asynchronously
        publishDeliveryEvent(saved);
        
        return mapToResponse(saved);
    }

    public DeliveryResponse getDeliveryDetails(String deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
        return mapToResponse(delivery);
    }

    private void publishDeliveryEvent(Delivery delivery) {
        com.foodpanda.deliverydispatchservice.dto.event.DeliveryEvent event = new com.foodpanda.deliverydispatchservice.dto.event.DeliveryEvent(
                delivery.getOrderId(),
                delivery.getId(),
                delivery.getRiderId(),
                delivery.getStatus()
        );
                
        rabbitTemplate.convertAndSend(com.foodpanda.deliverydispatchservice.config.RabbitMQConfig.DELIVERY_EXCHANGE, "delivery.updated", event);
        log.info("Published DeliveryEvent for order={} status={}", delivery.getOrderId(), delivery.getStatus());
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
