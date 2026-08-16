package com.foodpanda.deliverydispatchservice.exception;

public class RiderNotFoundException extends RuntimeException {
    public RiderNotFoundException(String id) {
        super("No rider found with given id: " + id);
    }
}
