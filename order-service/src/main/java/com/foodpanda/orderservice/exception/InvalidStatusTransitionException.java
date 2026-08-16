package com.foodpanda.orderservice.exception;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(String currentStatus, String targetStatus) {
        super("Cannot move from " + currentStatus + " to " + targetStatus);
    }
}
