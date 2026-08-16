package com.foodpanda.restaurantcatalogservice.exception;

public class RestaurantNotFoundException extends RuntimeException {

    public RestaurantNotFoundException(String restaurantId) {
        super("No restaurant found with given id: " + restaurantId);
    }
}
