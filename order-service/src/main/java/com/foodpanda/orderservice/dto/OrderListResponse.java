package com.foodpanda.orderservice.dto;

import java.time.Instant;

public class OrderListResponse {
    private String id;
    private String customerId;
    private String restaurantId;
    private Double totalAmount;
    private String status;
    private Instant createdAt;

    public OrderListResponse() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final OrderListResponse r = new OrderListResponse();
        public Builder id(String id) { r.id = id; return this; }
        public Builder customerId(String customerId) { r.customerId = customerId; return this; }
        public Builder restaurantId(String restaurantId) { r.restaurantId = restaurantId; return this; }
        public Builder totalAmount(Double totalAmount) { r.totalAmount = totalAmount; return this; }
        public Builder status(String status) { r.status = status; return this; }
        public Builder createdAt(Instant createdAt) { r.createdAt = createdAt; return this; }
        public OrderListResponse build() { return r; }
    }
}
