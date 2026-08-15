package com.foodpanda.cartservice.dto;

import java.time.Instant;

public class UpdateItemResponse {

    private String menuItemId;
    private String name;
    private Double price;
    private Integer quantity;
    private Double totalAmount;
    private Instant updatedAt;

    private UpdateItemResponse() {}

    public String getMenuItemId() { return menuItemId; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
    public Integer getQuantity() { return quantity; }
    public Double getTotalAmount() { return totalAmount; }
    public Instant getUpdatedAt() { return updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final UpdateItemResponse r = new UpdateItemResponse();
        public Builder menuItemId(String menuItemId) { r.menuItemId = menuItemId; return this; }
        public Builder name(String name) { r.name = name; return this; }
        public Builder price(Double price) { r.price = price; return this; }
        public Builder quantity(Integer quantity) { r.quantity = quantity; return this; }
        public Builder totalAmount(Double totalAmount) { r.totalAmount = totalAmount; return this; }
        public Builder updatedAt(Instant updatedAt) { r.updatedAt = updatedAt; return this; }
        public UpdateItemResponse build() { return r; }
    }
}
