package com.foodpanda.deliverydispatchservice.dto;

import com.foodpanda.deliverydispatchservice.model.RoutePoint;

import java.time.Instant;
import java.util.List;

public class DeliveryResponse {

    private String id;
    private String orderId;
    private String riderId;
    private String status;
    private List<RoutePoint> route;
    private Instant assignedAt;
    private Instant deliveredAt;

    public DeliveryResponse() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getRiderId() { return riderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<RoutePoint> getRoute() { return route; }
    public void setRoute(List<RoutePoint> route) { this.route = route; }

    public Instant getAssignedAt() { return assignedAt; }
    public void setAssignedAt(Instant assignedAt) { this.assignedAt = assignedAt; }

    public Instant getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final DeliveryResponse r = new DeliveryResponse();
        public Builder id(String id) { r.id = id; return this; }
        public Builder orderId(String orderId) { r.orderId = orderId; return this; }
        public Builder riderId(String riderId) { r.riderId = riderId; return this; }
        public Builder status(String status) { r.status = status; return this; }
        public Builder route(List<RoutePoint> route) { r.route = route; return this; }
        public Builder assignedAt(Instant assignedAt) { r.assignedAt = assignedAt; return this; }
        public Builder deliveredAt(Instant deliveredAt) { r.deliveredAt = deliveredAt; return this; }
        public DeliveryResponse build() { return r; }
    }
}
