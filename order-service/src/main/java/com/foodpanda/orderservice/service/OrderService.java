package com.foodpanda.orderservice.service;

import com.foodpanda.orderservice.dto.*;
import com.foodpanda.orderservice.exception.InvalidStatusTransitionException;
import com.foodpanda.orderservice.exception.MenuItemUnavailableException;
import com.foodpanda.orderservice.exception.OrderNotFoundException;
import com.foodpanda.orderservice.model.*;
import com.foodpanda.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${service.restaurant.url}")
    private String restaurantServiceUrl;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public OrderResponse placeOrder(String customerId, PlaceOrderRequest request, String token) {
        // Here we would typically make synchronous calls to other services
        // Example: Validate restaurant and items with restaurant-catalog-service
        // For simplicity, we are assuming items are valid.
        // In a real app, you'd check:
        // 1. Restaurant exists and is open
        // 2. All items exist and prices match
        
        // Simulating validation for now
        // if (!validateItems(request.getRestaurantId(), request.getItems(), token)) {
        //     throw new MenuItemUnavailableException();
        // }

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setRestaurantId(request.getRestaurantId());
        
        List<OrderItem> items = request.getItems().stream()
                .map(item -> new OrderItem(item.getMenuItemId(), item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toList());
        order.setItems(items);

        DeliveryAddress address = new DeliveryAddress(
                request.getDeliveryAddress().getStreet(),
                request.getDeliveryAddress().getCity(),
                request.getDeliveryAddress().getLat(),
                request.getDeliveryAddress().getLng()
        );
        order.setDeliveryAddress(address);

        double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        order.setTotalAmount(total);

        order.setStatus(OrderStatus.PLACED.name());
        order.getStatusHistory().add(new StatusHistoryEntry(OrderStatus.PLACED.name(), Instant.now()));
        
        order.setPaymentStatus(PaymentStatus.PENDING.name());

        Order saved = orderRepository.save(order);
        log.info("Placed order id={} for customerId={}", saved.getId(), customerId);

        return mapToResponse(saved);
    }

    public OrderResponse getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return mapToResponse(order);
    }

    public Page<OrderListResponse> listMyOrders(String customerId, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orders;
        
        if (status != null && !status.isEmpty()) {
            orders = orderRepository.findByCustomerIdAndStatus(customerId, status, pageable);
        } else {
            orders = orderRepository.findByCustomerId(customerId, pageable);
        }

        return orders.map(this::mapToListResponse);
    }

    public OrderResponse updateOrderStatus(String orderId, UpdateStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderStatus currentStatus = OrderStatus.valueOf(order.getStatus());
        OrderStatus newStatus = OrderStatus.valueOf(request.getStatus());

        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new InvalidStatusTransitionException(currentStatus.name(), newStatus.name());
        }

        order.setStatus(newStatus.name());
        order.getStatusHistory().add(new StatusHistoryEntry(newStatus.name(), Instant.now()));
        
        Order saved = orderRepository.save(order);
        log.info("Updated order id={} status to {}", saved.getId(), newStatus.name());

        return mapToResponse(saved);
    }

    public List<OrderListResponse> listRestaurantOrders(String restaurantId, String status) {
        List<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderRepository.findByRestaurantIdAndStatus(restaurantId, status);
        } else {
            orders = orderRepository.findByRestaurantId(restaurantId);
        }

        return orders.stream().map(this::mapToListResponse).collect(Collectors.toList());
    }

    // ── Mappers ─────────────────────────────────────────────────────────────

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .riderId(order.getRiderId())
                .items(order.getItems())
                .deliveryAddress(order.getDeliveryAddress())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .statusHistory(order.getStatusHistory())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private OrderListResponse mapToListResponse(Order order) {
        return OrderListResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
