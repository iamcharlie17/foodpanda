package com.foodpanda.notificationservice.exception;

public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(String notificationId) {
        super("No notification found with given id: " + notificationId);
    }
}
