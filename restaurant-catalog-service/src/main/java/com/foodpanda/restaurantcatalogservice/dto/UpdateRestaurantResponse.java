package com.foodpanda.restaurantcatalogservice.dto;

import java.time.Instant;

/**
 * Slim response for PUT /api/restaurants/{id} — only returns changed fields + updatedAt.
 */
public class UpdateRestaurantResponse {

    private String id;
    private String description;
    private Boolean isOpen;
    private OperatingHoursDto operatingHours;
    private Instant updatedAt;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsOpen() { return isOpen; }
    public void setIsOpen(Boolean isOpen) { this.isOpen = isOpen; }

    public OperatingHoursDto getOperatingHours() { return operatingHours; }
    public void setOperatingHours(OperatingHoursDto operatingHours) { this.operatingHours = operatingHours; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final UpdateRestaurantResponse r = new UpdateRestaurantResponse();

        public Builder id(String id) { r.id = id; return this; }
        public Builder description(String description) { r.description = description; return this; }
        public Builder isOpen(Boolean isOpen) { r.isOpen = isOpen; return this; }
        public Builder operatingHours(OperatingHoursDto operatingHours) { r.operatingHours = operatingHours; return this; }
        public Builder updatedAt(Instant updatedAt) { r.updatedAt = updatedAt; return this; }
        public UpdateRestaurantResponse build() { return r; }
    }
}
