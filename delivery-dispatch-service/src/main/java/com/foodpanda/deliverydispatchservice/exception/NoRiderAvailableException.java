package com.foodpanda.deliverydispatchservice.exception;

public class NoRiderAvailableException extends RuntimeException {
    public NoRiderAvailableException() {
        super("No available rider found near restaurant location");
    }
}
