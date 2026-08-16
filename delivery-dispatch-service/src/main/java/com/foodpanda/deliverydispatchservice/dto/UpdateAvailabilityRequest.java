package com.foodpanda.deliverydispatchservice.dto;

import jakarta.validation.constraints.NotNull;

public class UpdateAvailabilityRequest {

    @NotNull(message = "isAvailable is required")
    private Boolean isAvailable;

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
}
