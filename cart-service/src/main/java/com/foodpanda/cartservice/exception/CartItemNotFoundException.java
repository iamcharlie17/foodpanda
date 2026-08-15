package com.foodpanda.cartservice.exception;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(String menuItemId) {
        super("No cart item found with menu item id: " + menuItemId);
    }
}
