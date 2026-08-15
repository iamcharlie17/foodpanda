package com.foodpanda.restaurantcatalogservice.dto;

import java.time.Instant;

/**
 * Full menu item response — used for POST 201 and list responses.
 */
public class MenuItemResponse {

    private String id;
    private String restaurantId;
    private String name;
    private String description;
    private String category;
    private Double price;
    private Boolean isAvailable;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final MenuItemResponse item = new MenuItemResponse();

        public Builder id(String id) { item.id = id; return this; }
        public Builder restaurantId(String restaurantId) { item.restaurantId = restaurantId; return this; }
        public Builder name(String name) { item.name = name; return this; }
        public Builder description(String description) { item.description = description; return this; }
        public Builder category(String category) { item.category = category; return this; }
        public Builder price(Double price) { item.price = price; return this; }
        public Builder isAvailable(Boolean isAvailable) { item.isAvailable = isAvailable; return this; }
        public Builder imageUrl(String imageUrl) { item.imageUrl = imageUrl; return this; }
        public Builder createdAt(Instant createdAt) { item.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { item.updatedAt = updatedAt; return this; }
        public MenuItemResponse build() { return item; }
    }
}
