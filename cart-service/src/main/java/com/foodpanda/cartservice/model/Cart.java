package com.foodpanda.cartservice.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    @Indexed(unique = true)
    private String customerId;

    private String restaurantId;

    private List<CartItem> items = new ArrayList<>();

    private Double totalAmount = 0.0;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Cart() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Cart cart = new Cart();

        public Builder id(String id) { cart.id = id; return this; }
        public Builder customerId(String customerId) { cart.customerId = customerId; return this; }
        public Builder restaurantId(String restaurantId) { cart.restaurantId = restaurantId; return this; }
        public Builder items(List<CartItem> items) { cart.items = items; return this; }
        public Builder totalAmount(Double totalAmount) { cart.totalAmount = totalAmount; return this; }
        public Cart build() { return cart; }
    }
}
