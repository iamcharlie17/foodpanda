package com.foodpanda.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PlaceOrderRequest {

    @NotBlank(message = "restaurantId is required")
    private String restaurantId;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<OrderItemRequest> items;

    @NotNull(message = "Delivery address is required")
    @Valid
    private DeliveryAddressRequest deliveryAddress;

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }

    public DeliveryAddressRequest getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(DeliveryAddressRequest deliveryAddress) { this.deliveryAddress = deliveryAddress; }
}
