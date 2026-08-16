package com.foodpanda.deliverydispatchservice.model;

import java.time.Instant;

public class RoutePoint {

    private Double lat;
    private Double lng;
    private Instant timestamp;

    public RoutePoint() {}

    public RoutePoint(Double lat, Double lng, Instant timestamp) {
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
    }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
