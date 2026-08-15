package com.foodpanda.restaurantcatalogservice.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "menu_items")
public class MenuItem {

    @Id
    private String id;

    private String restaurantId;

    private String name;

    private String description;

    private String category;

    private Double price;

    @Field("isAvailable")
    private Boolean isAvailable = true;

    private String imageUrl;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public MenuItem() {}

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
        private final MenuItem item = new MenuItem();

        public Builder restaurantId(String restaurantId) { item.restaurantId = restaurantId; return this; }
        public Builder name(String name) { item.name = name; return this; }
        public Builder description(String description) { item.description = description; return this; }
        public Builder category(String category) { item.category = category; return this; }
        public Builder price(Double price) { item.price = price; return this; }
        public Builder isAvailable(Boolean isAvailable) { item.isAvailable = isAvailable; return this; }
        public Builder imageUrl(String imageUrl) { item.imageUrl = imageUrl; return this; }
        public MenuItem build() { return item; }
    }
}
