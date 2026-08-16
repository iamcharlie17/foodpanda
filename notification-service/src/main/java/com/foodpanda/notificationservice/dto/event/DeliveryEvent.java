package com.foodpanda.notificationservice.dto.event;

public class DeliveryEvent {
    private String orderId;
    private String deliveryId;
    private String riderId;
    private String status;

    public DeliveryEvent() {}

    public DeliveryEvent(String orderId, String deliveryId, String riderId, String status) {
        this.orderId = orderId;
        this.deliveryId = deliveryId;
        this.riderId = riderId;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getDeliveryId() { return deliveryId; }
    public void setDeliveryId(String deliveryId) { this.deliveryId = deliveryId; }
    public String getRiderId() { return riderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
