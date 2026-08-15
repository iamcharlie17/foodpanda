package com.foodpanda.restaurantcatalogservice.dto;

import java.time.Instant;
import java.util.List;

public class RestaurantResponse {

    private String id;
    private String ownerId;
    private String name;
    private String description;
    private List<String> cuisine;
    private AddressDto address;
    private OperatingHoursDto operatingHours;
    private Double rating;
    private Boolean isOpen;
    private Boolean isApproved;
    private Instant createdAt;
    private Instant updatedAt;

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

    public AddressDto getAddress() { return address; }
    public void setAddress(AddressDto address) { this.address = address; }

    public OperatingHoursDto getOperatingHours() { return operatingHours; }
    public void setOperatingHours(OperatingHoursDto operatingHours) { this.operatingHours = operatingHours; }

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
        private final RestaurantResponse r = new RestaurantResponse();

        public Builder id(String id) { r.id = id; return this; }
        public Builder ownerId(String ownerId) { r.ownerId = ownerId; return this; }
        public Builder name(String name) { r.name = name; return this; }
        public Builder description(String description) { r.description = description; return this; }
        public Builder cuisine(List<String> cuisine) { r.cuisine = cuisine; return this; }
        public Builder address(AddressDto address) { r.address = address; return this; }
        public Builder operatingHours(OperatingHoursDto operatingHours) { r.operatingHours = operatingHours; return this; }
        public Builder rating(Double rating) { r.rating = rating; return this; }
        public Builder isOpen(Boolean isOpen) { r.isOpen = isOpen; return this; }
        public Builder isApproved(Boolean isApproved) { r.isApproved = isApproved; return this; }
        public Builder createdAt(Instant createdAt) { r.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { r.updatedAt = updatedAt; return this; }
        public RestaurantResponse build() { return r; }
    }
}
