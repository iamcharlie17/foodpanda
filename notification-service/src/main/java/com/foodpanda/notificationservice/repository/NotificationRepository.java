package com.foodpanda.notificationservice.repository;

import com.foodpanda.notificationservice.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    /**
     * Fetch all notifications for a user, ordered by createdAt descending (newest first).
     */
    Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    /**
     * Fetch notifications for a user filtered by isRead status.
     */
    Page<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(String userId,
                                                                  Boolean isRead,
                                                                  Pageable pageable);

    /**
     * Count unread (or all) notifications for a user — used to derive updatedCount for read-all.
     */
    long countByUserIdAndIsRead(String userId, Boolean isRead);
}
