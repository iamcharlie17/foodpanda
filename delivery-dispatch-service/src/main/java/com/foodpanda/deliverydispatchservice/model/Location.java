package com.foodpanda.deliverydispatchservice.model;

import java.time.Instant;

public class Location {

    private Double lat;
    private Double lng;
    private Instant updatedAt;

    public Location() {}

    public Location(Double lat, Double lng, Instant updatedAt) {
        this.lat = lat;
        this.lng = lng;
        this.updatedAt = updatedAt;
    }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
