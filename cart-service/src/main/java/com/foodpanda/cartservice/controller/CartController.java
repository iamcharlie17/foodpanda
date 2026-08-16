package com.foodpanda.cartservice.controller;

import com.foodpanda.cartservice.dto.*;
import com.foodpanda.cartservice.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /** GET /api/cart */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal String customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    /** POST /api/cart/items */
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @AuthenticationPrincipal String customerId,
            @Valid @RequestBody AddItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(customerId, request));
    }

    /** PUT /api/cart/items/{menuItemId} */
    @PutMapping("/items/{menuItemId}")
    public ResponseEntity<UpdateItemResponse> updateItemQuantity(
            @AuthenticationPrincipal String customerId,
            @PathVariable String menuItemId,
            @Valid @RequestBody UpdateQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(customerId, menuItemId, request));
    }

    /** DELETE /api/cart/items/{menuItemId} */
    @DeleteMapping("/items/{menuItemId}")
    public ResponseEntity<RemoveItemResponse> removeItem(
            @AuthenticationPrincipal String customerId,
            @PathVariable String menuItemId) {
        return ResponseEntity.ok(cartService.removeItem(customerId, menuItemId));
    }

    /** DELETE /api/cart */
    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearCart(
            @AuthenticationPrincipal String customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.ok(Map.of("message", "Cart cleared successfully"));
    }
}
