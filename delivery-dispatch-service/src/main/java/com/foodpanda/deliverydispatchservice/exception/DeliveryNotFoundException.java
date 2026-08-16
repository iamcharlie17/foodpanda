package com.foodpanda.deliverydispatchservice.exception;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(String id) {
        super("No delivery found for given id: " + id);
    }
}
