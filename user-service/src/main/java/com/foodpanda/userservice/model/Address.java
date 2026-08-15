package com.foodpanda.userservice.model;

public class Address {

    private String label;
    private String street;
    private String city;
    private Double lat;
    private Double lng;
    private Boolean isDefault;

    public Address() {}

    public Address(String label, String street, String city,
                   Double lat, Double lng, Boolean isDefault) {
        this.label = label;
        this.street = street;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
        this.isDefault = isDefault;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }

    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
}
