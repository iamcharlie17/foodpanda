package com.foodpanda.deliverydispatchservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateDeliveryStatusRequest {

    @NotBlank(message = "status is required")
    private String status;

    @NotNull(message = "location is required")
    private LocationDto location;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocationDto getLocation() { return location; }
    public void setLocation(LocationDto location) { this.location = location; }
}
