package com.foodpanda.cartservice.dto;

import com.foodpanda.cartservice.model.CartItem;

import java.time.Instant;
import java.util.List;

public class CartResponse {

    private String id;
    private String customerId;
    private String restaurantId;
    private List<CartItem> items;
    private Double totalAmount;
    private Instant updatedAt;

    private CartResponse() {}

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getRestaurantId() { return restaurantId; }
    public List<CartItem> getItems() { return items; }
    public Double getTotalAmount() { return totalAmount; }
    public Instant getUpdatedAt() { return updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final CartResponse r = new CartResponse();
        public Builder id(String id) { r.id = id; return this; }
        public Builder customerId(String customerId) { r.customerId = customerId; return this; }
        public Builder restaurantId(String restaurantId) { r.restaurantId = restaurantId; return this; }
        public Builder items(List<CartItem> items) { r.items = items; return this; }
        public Builder totalAmount(Double totalAmount) { r.totalAmount = totalAmount; return this; }
        public Builder updatedAt(Instant updatedAt) { r.updatedAt = updatedAt; return this; }
        public CartResponse build() { return r; }
    }
}
