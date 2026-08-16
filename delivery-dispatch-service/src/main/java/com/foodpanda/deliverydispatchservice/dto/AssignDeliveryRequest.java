package com.foodpanda.deliverydispatchservice.dto;

import jakarta.validation.constraints.NotBlank;

public class AssignDeliveryRequest {

    @NotBlank(message = "orderId is required")
    private String orderId;

    @NotBlank(message = "riderId is required")
    private String riderId;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getRiderId() { return riderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }
}
