package com.foodpanda.cartservice.service;

import com.foodpanda.cartservice.dto.*;
import com.foodpanda.cartservice.exception.CartItemNotFoundException;
import com.foodpanda.cartservice.exception.DifferentRestaurantException;
import com.foodpanda.cartservice.model.Cart;
import com.foodpanda.cartservice.model.CartItem;
import com.foodpanda.cartservice.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${service.restaurant.url}")
    private String restaurantServiceUrl;

    public CartService(CartRepository cartRepository, RestTemplate restTemplate) {
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
    }

    // ─────────────────────────────────────────────────────────────────────────

    public CartResponse getCart(String customerId) {
        Optional<Cart> existing = cartRepository.findByCustomerId(customerId);

        if (existing.isEmpty()) {
            return CartResponse.builder()
                    .customerId(customerId)
                    .restaurantId(null)
                    .items(java.util.List.of())
                    .totalAmount(0.0)
                    .build();
        }

        return toCartResponse(existing.get());
    }

    public CartResponse addItem(String customerId, AddItemRequest request) {
        // Validate with restaurant catalog service
        String url = restaurantServiceUrl + "/api/restaurants/" + request.getRestaurantId() + "/menu-items";
        MenuItemResponse[] items;
        try {
            items = restTemplate.getForObject(url, MenuItemResponse[].class);
        } catch (Exception e) {
            throw new com.foodpanda.cartservice.exception.InvalidItemException("Restaurant not found or unavailable.");
        }

        if (items == null) {
            throw new com.foodpanda.cartservice.exception.InvalidItemException("Restaurant not found or unavailable.");
        }

        MenuItemResponse validItem = java.util.Arrays.stream(items)
                .filter(item -> item.getId().equals(request.getMenuItemId()))
                .findFirst()
                .orElseThrow(() -> new com.foodpanda.cartservice.exception.InvalidItemException("Menu item not found in this restaurant."));

        if (validItem.getIsAvailable() == null || !validItem.getIsAvailable()) {
            throw new com.foodpanda.cartservice.exception.InvalidItemException("Menu item is currently unavailable.");
        }

        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> Cart.builder().customerId(customerId).build());

        if (!cart.getItems().isEmpty() && cart.getRestaurantId() != null
                && !cart.getRestaurantId().equals(request.getRestaurantId())) {
            throw new DifferentRestaurantException();
        }

        cart.setRestaurantId(request.getRestaurantId());

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getMenuItemId().equals(request.getMenuItemId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            // Use validated name and price
            item.setName(validItem.getName());
            item.setPrice(validItem.getPrice());
        } else {
            cart.getItems().add(new CartItem(
                    request.getMenuItemId(),
                    validItem.getName(),
                    validItem.getPrice(),
                    request.getQuantity()
            ));
        }

        recalculateTotal(cart);
        Cart saved = cartRepository.save(cart);
        log.info("Item {} added to cart for customer={}", request.getMenuItemId(), customerId);

        return toCartResponse(saved);
    }

    public UpdateItemResponse updateItemQuantity(String customerId, String menuItemId, UpdateQuantityRequest request) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartItemNotFoundException(menuItemId));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getMenuItemId().equals(menuItemId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException(menuItemId));

        item.setQuantity(request.getQuantity());
        recalculateTotal(cart);
        Cart saved = cartRepository.save(cart);

        return UpdateItemResponse.builder()
                .menuItemId(item.getMenuItemId())
                .name(item.getName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .totalAmount(saved.getTotalAmount())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public RemoveItemResponse removeItem(String customerId, String menuItemId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartItemNotFoundException(menuItemId));

        boolean removed = cart.getItems().removeIf(i -> i.getMenuItemId().equals(menuItemId));
        if (!removed) {
            throw new CartItemNotFoundException(menuItemId);
        }

        if (cart.getItems().isEmpty()) {
            cart.setRestaurantId(null);
        }

        recalculateTotal(cart);
        Cart saved = cartRepository.save(cart);

        return RemoveItemResponse.builder()
                .message("Item removed from cart")
                .items(saved.getItems())
                .totalAmount(saved.getTotalAmount())
                .build();
    }

    public void clearCart(String customerId) {
        cartRepository.findByCustomerId(customerId).ifPresent(cart -> {
            cart.getItems().clear();
            cart.setRestaurantId(null);
            recalculateTotal(cart);
            cartRepository.save(cart);
        });
        log.info("Cart cleared for customer={}", customerId);
    }

    // ─────────────────────────────────────────────────────────────────────────

    private void recalculateTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        cart.setTotalAmount(total);
    }

    private CartResponse toCartResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .customerId(cart.getCustomerId())
                .restaurantId(cart.getRestaurantId())
                .items(cart.getItems())
                .totalAmount(cart.getTotalAmount())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}
