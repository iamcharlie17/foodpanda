package com.foodpanda.restaurantcatalogservice.exception;

public class MenuItemNotFoundException extends RuntimeException {

    public MenuItemNotFoundException(String itemId) {
        super("No menu item found with given id: " + itemId);
    }
}
