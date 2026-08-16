package com.foodpanda.orderservice.controller;

import com.foodpanda.orderservice.dto.*;
import com.foodpanda.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal String customerId,
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody PlaceOrderRequest request) {
        
        String token = authHeader.replace("Bearer ", "");
        OrderResponse response = orderService.placeOrder(customerId, request, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping
    public ResponseEntity<Page<OrderListResponse>> listMyOrders(
            @AuthenticationPrincipal String customerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        return ResponseEntity.ok(orderService.listMyOrders(customerId, status, page, size));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateStatusRequest request) {
        
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
    }
}
