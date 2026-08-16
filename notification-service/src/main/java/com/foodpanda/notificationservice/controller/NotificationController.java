package com.foodpanda.notificationservice.controller;

import com.foodpanda.notificationservice.dto.MarkReadResponse;
import com.foodpanda.notificationservice.dto.PagedNotificationResponse;
import com.foodpanda.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * GET /api/notifications?isRead=false&page=0&size=10
     * userId is obtained from the JWT security context — never from a query param.
     */
    @GetMapping
    public ResponseEntity<PagedNotificationResponse> listNotifications(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(notificationService.listNotifications(userId, isRead, page, size));
    }

    /**
     * PATCH /api/notifications/{id}/read
     * Only marks the notification as read if it belongs to the authenticated user.
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<MarkReadResponse> markAsRead(
            @AuthenticationPrincipal String userId,
            @PathVariable String id) {
        return ResponseEntity.ok(notificationService.markAsRead(userId, id));
    }

    /**
     * PATCH /api/notifications/read-all
     * Marks all notifications belonging to the authenticated user as read.
     */
    @PatchMapping("/read-all")
    public ResponseEntity<Map<String, Object>> markAllAsRead(
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(notificationService.markAllAsRead(userId));
    }
}
