package com.foodpanda.restaurantcatalogservice.dto;

import java.util.List;

/**
 * Slim summary used in the paginated list/browse response.
 */
public class RestaurantSummaryResponse {

    private String id;
    private String name;
    private List<String> cuisine;
    private String city;
    private Double rating;
    private Boolean isOpen;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getCuisine() { return cuisine; }
    public void setCuisine(List<String> cuisine) { this.cuisine = cuisine; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Boolean getIsOpen() { return isOpen; }
    public void setIsOpen(Boolean isOpen) { this.isOpen = isOpen; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final RestaurantSummaryResponse r = new RestaurantSummaryResponse();

        public Builder id(String id) { r.id = id; return this; }
        public Builder name(String name) { r.name = name; return this; }
        public Builder cuisine(List<String> cuisine) { r.cuisine = cuisine; return this; }
        public Builder city(String city) { r.city = city; return this; }
        public Builder rating(Double rating) { r.rating = rating; return this; }
        public Builder isOpen(Boolean isOpen) { r.isOpen = isOpen; return this; }
        public RestaurantSummaryResponse build() { return r; }
    }
}
