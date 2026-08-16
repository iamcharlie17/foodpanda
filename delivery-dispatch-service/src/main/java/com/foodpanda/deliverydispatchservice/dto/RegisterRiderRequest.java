package com.foodpanda.deliverydispatchservice.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRiderRequest {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "vehicleType is required")
    private String vehicleType;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
}
