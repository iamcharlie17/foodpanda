package com.foodpanda.orderservice.dto;

import com.foodpanda.orderservice.model.DeliveryAddress;
import com.foodpanda.orderservice.model.OrderItem;
import com.foodpanda.orderservice.model.StatusHistoryEntry;

import java.time.Instant;
import java.util.List;

public class OrderResponse {

    private String id;
    private String customerId;
    private String restaurantId;
    private String riderId;
    private List<OrderItem> items;
    private DeliveryAddress deliveryAddress;
    private Double totalAmount;
    private String status;
    private List<StatusHistoryEntry> statusHistory;
    private String paymentStatus;
    private Instant createdAt;
    private Instant updatedAt;

    public OrderResponse() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getRiderId() { return riderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public DeliveryAddress getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(DeliveryAddress deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<StatusHistoryEntry> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<StatusHistoryEntry> statusHistory) { this.statusHistory = statusHistory; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final OrderResponse r = new OrderResponse();
        public Builder id(String id) { r.id = id; return this; }
        public Builder customerId(String customerId) { r.customerId = customerId; return this; }
        public Builder restaurantId(String restaurantId) { r.restaurantId = restaurantId; return this; }
        public Builder riderId(String riderId) { r.riderId = riderId; return this; }
        public Builder items(List<OrderItem> items) { r.items = items; return this; }
        public Builder deliveryAddress(DeliveryAddress deliveryAddress) { r.deliveryAddress = deliveryAddress; return this; }
        public Builder totalAmount(Double totalAmount) { r.totalAmount = totalAmount; return this; }
        public Builder status(String status) { r.status = status; return this; }
        public Builder statusHistory(List<StatusHistoryEntry> statusHistory) { r.statusHistory = statusHistory; return this; }
        public Builder paymentStatus(String paymentStatus) { r.paymentStatus = paymentStatus; return this; }
        public Builder createdAt(Instant createdAt) { r.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { r.updatedAt = updatedAt; return this; }
        public OrderResponse build() { return r; }
    }
}
