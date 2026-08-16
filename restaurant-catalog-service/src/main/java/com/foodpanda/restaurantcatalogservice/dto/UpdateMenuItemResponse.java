package com.foodpanda.restaurantcatalogservice.dto;

import java.time.Instant;

/**
 * Slim response for PUT /api/restaurants/{id}/menu-items/{itemId}.
 */
public class UpdateMenuItemResponse {

    private String id;
    private Double price;
    private Boolean isAvailable;
    private Instant updatedAt;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final UpdateMenuItemResponse r = new UpdateMenuItemResponse();

        public Builder id(String id) { r.id = id; return this; }
        public Builder price(Double price) { r.price = price; return this; }
        public Builder isAvailable(Boolean isAvailable) { r.isAvailable = isAvailable; return this; }
        public Builder updatedAt(Instant updatedAt) { r.updatedAt = updatedAt; return this; }
        public UpdateMenuItemResponse build() { return r; }
    }
}
