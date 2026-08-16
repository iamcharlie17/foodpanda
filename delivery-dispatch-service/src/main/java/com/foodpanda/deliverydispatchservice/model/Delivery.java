package com.foodpanda.deliverydispatchservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "deliveries")
public class Delivery {

    @Id
    private String id;

    @Indexed(unique = true)
    private String orderId;

    @Indexed
    private String riderId;

    private String status;

    private List<RoutePoint> route = new ArrayList<>();

    private Instant assignedAt;

    private Instant deliveredAt;

    public Delivery() {}

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
}
