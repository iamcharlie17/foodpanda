package com.foodpanda.deliverydispatchservice.exception;

public class InvalidDeliveryStatusTransitionException extends RuntimeException {
    public InvalidDeliveryStatusTransitionException(String currentStatus, String targetStatus) {
        super("Cannot move from " + currentStatus + " to " + targetStatus);
    }
}
