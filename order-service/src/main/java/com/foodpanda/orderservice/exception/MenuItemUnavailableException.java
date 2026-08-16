package com.foodpanda.orderservice.exception;

public class MenuItemUnavailableException extends RuntimeException {
    public MenuItemUnavailableException() {
        super("One or more items are no longer available");
    }
}
