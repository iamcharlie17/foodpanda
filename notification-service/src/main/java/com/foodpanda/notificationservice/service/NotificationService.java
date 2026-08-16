package com.foodpanda.notificationservice.service;

import com.foodpanda.notificationservice.dto.*;
import com.foodpanda.notificationservice.exception.NotificationNotFoundException;
import com.foodpanda.notificationservice.model.Notification;
import com.foodpanda.notificationservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Internal — create notification
    // ─────────────────────────────────────────────────────────────────────────

    public NotificationResponse createNotification(CreateNotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .channel(request.getChannel())
                .message(request.getMessage())
                .isRead(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Created notification id={} userId={} type={}", saved.getId(), saved.getUserId(), saved.getType());

        return toNotificationResponse(saved);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // User-facing — list notifications
    // ─────────────────────────────────────────────────────────────────────────

    public PagedNotificationResponse listNotifications(String userId, Boolean isRead, int page, int size) {
        Page<Notification> resultPage;

        if (isRead != null) {
            resultPage = notificationRepository
                    .findByUserIdAndIsReadOrderByCreatedAtDesc(userId, isRead, PageRequest.of(page, size));
        } else {
            resultPage = notificationRepository
                    .findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size));
        }

        List<NotificationSummaryResponse> content = resultPage.getContent().stream()
                .map(this::toSummaryResponse)
                .toList();

        return new PagedNotificationResponse(
                content,
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getTotalElements(),
                resultPage.getTotalPages()
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // User-facing — mark single notification as read
    // ─────────────────────────────────────────────────────────────────────────

    public MarkReadResponse markAsRead(String userId, String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        // Ensure the notification belongs to the authenticated user
        if (!notification.getUserId().equals(userId)) {
            throw new NotificationNotFoundException(notificationId);
        }

        notification.setIsRead(true);
        Notification saved = notificationRepository.save(notification);

        return new MarkReadResponse(saved.getId(), saved.getIsRead());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // User-facing — mark all notifications as read
    // ─────────────────────────────────────────────────────────────────────────

    public Map<String, Object> markAllAsRead(String userId) {
        long unreadCount = notificationRepository.countByUserIdAndIsRead(userId, false);

        if (unreadCount > 0) {
            // Fetch all unread for this user and batch-update
            Page<Notification> allUnread = notificationRepository
                    .findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false,
                            PageRequest.of(0, (int) unreadCount));

            List<Notification> toUpdate = allUnread.getContent().stream()
                    .peek(n -> n.setIsRead(true))
                    .toList();

            notificationRepository.saveAll(toUpdate);
        }

        log.info("Marked {} notifications as read for userId={}", unreadCount, userId);

        return Map.of(
                "message",      "All notifications marked as read",
                "updatedCount", unreadCount
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private NotificationResponse toNotificationResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .userId(n.getUserId())
                .type(n.getType())
                .channel(n.getChannel())
                .message(n.getMessage())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }

    private NotificationSummaryResponse toSummaryResponse(Notification n) {
        return NotificationSummaryResponse.builder()
                .id(n.getId())
                .type(n.getType())
                .channel(n.getChannel())
                .message(n.getMessage())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
