package com.foodpanda.restaurantcatalogservice.dto;

public class UpdateRestaurantRequest {

    private String description;
    private Boolean isOpen;
    private OperatingHoursDto operatingHours;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsOpen() { return isOpen; }
    public void setIsOpen(Boolean isOpen) { this.isOpen = isOpen; }

    public OperatingHoursDto getOperatingHours() { return operatingHours; }
    public void setOperatingHours(OperatingHoursDto operatingHours) { this.operatingHours = operatingHours; }
}
