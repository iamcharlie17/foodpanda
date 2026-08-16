package com.foodpanda.orderservice.controller;

import com.foodpanda.orderservice.dto.OrderListResponse;
import com.foodpanda.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantOrderController {

    private final OrderService orderService;

    public RestaurantOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{restaurantId}/orders")
    public ResponseEntity<List<OrderListResponse>> listRestaurantOrders(
            @PathVariable String restaurantId,
            @RequestParam(required = false) String status) {
        
        return ResponseEntity.ok(orderService.listRestaurantOrders(restaurantId, status));
    }
}
