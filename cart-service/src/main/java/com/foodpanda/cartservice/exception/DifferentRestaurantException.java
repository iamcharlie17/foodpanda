package com.foodpanda.cartservice.exception;

public class DifferentRestaurantException extends RuntimeException {

    public DifferentRestaurantException() {
        super("Cart already contains items from another restaurant. Clear cart to continue.");
    }
}
