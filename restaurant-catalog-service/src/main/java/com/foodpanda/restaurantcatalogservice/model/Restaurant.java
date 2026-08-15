package com.foodpanda.restaurantcatalogservice.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String id;

    private String ownerId;

    private String name;

    private String description;

    private List<String> cuisine = new ArrayList<>();

    private Address address;

    private OperatingHours operatingHours;

    private Double rating = 0.0;

    @Field("isOpen")
    private Boolean isOpen = true;

    @Field("isApproved")
    private Boolean isApproved = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Restaurant() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getCuisine() { return cuisine; }
    public void setCuisine(List<String> cuisine) { this.cuisine = cuisine; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public OperatingHours getOperatingHours() { return operatingHours; }
    public void setOperatingHours(OperatingHours operatingHours) { this.operatingHours = operatingHours; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Boolean getIsOpen() { return isOpen; }
    public void setIsOpen(Boolean isOpen) { this.isOpen = isOpen; }

    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Restaurant r = new Restaurant();

        public Builder ownerId(String ownerId) { r.ownerId = ownerId; return this; }
        public Builder name(String name) { r.name = name; return this; }
        public Builder description(String description) { r.description = description; return this; }
        public Builder cuisine(List<String> cuisine) { r.cuisine = cuisine; return this; }
        public Builder address(Address address) { r.address = address; return this; }
        public Builder operatingHours(OperatingHours operatingHours) { r.operatingHours = operatingHours; return this; }
        public Builder rating(Double rating) { r.rating = rating; return this; }
        public Builder isOpen(Boolean isOpen) { r.isOpen = isOpen; return this; }
        public Builder isApproved(Boolean isApproved) { r.isApproved = isApproved; return this; }
        public Restaurant build() { return r; }
    }
}
