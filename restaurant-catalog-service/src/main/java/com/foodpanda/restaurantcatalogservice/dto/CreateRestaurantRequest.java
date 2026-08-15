package com.foodpanda.restaurantcatalogservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateRestaurantRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private List<String> cuisine;

    @Valid
    @NotNull(message = "Address is required")
    private AddressDto address;

    @Valid
    private OperatingHoursDto operatingHours;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getCuisine() { return cuisine; }
    public void setCuisine(List<String> cuisine) { this.cuisine = cuisine; }

    public AddressDto getAddress() { return address; }
    public void setAddress(AddressDto address) { this.address = address; }

    public OperatingHoursDto getOperatingHours() { return operatingHours; }
    public void setOperatingHours(OperatingHoursDto operatingHours) { this.operatingHours = operatingHours; }
}
