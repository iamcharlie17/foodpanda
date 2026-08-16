package com.foodpanda.notificationservice.controller;

import com.foodpanda.notificationservice.dto.CreateNotificationRequest;
import com.foodpanda.notificationservice.dto.NotificationResponse;
import com.foodpanda.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Internal endpoint consumed exclusively by other microservices
 * (Order Service, Delivery Service, Payment Service).
 * Permit-all in SecurityConfig — no user JWT required.
 * Accepts userId in the request body because the calling service supplies it.
 */
@RestController
@RequestMapping("/internal")
public class InternalNotificationController {

    private final NotificationService notificationService;

    public InternalNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /** POST /internal/notifications */
    @PostMapping("/notifications")
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.createNotification(request));
    }
}
