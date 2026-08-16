package com.foodpanda.orderservice.model;

/**
 * Embedded delivery address inside an order.
 */
public class DeliveryAddress {

    private String street;
    private String city;
    private Double lat;
    private Double lng;

    public DeliveryAddress() {}

    public DeliveryAddress(String street, String city, Double lat, Double lng) {
        this.street = street;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
    }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }
}
