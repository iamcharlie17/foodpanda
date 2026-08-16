package com.foodpanda.orderservice.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String id) {
        super("No order found with given id");
    }
}
