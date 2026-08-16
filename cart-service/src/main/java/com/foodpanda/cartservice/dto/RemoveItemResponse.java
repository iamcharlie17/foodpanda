package com.foodpanda.cartservice.dto;

import com.foodpanda.cartservice.model.CartItem;

import java.util.List;

public class RemoveItemResponse {

    private String message;
    private List<CartItem> items;
    private Double totalAmount;

    private RemoveItemResponse() {}

    public String getMessage() { return message; }
    public List<CartItem> getItems() { return items; }
    public Double getTotalAmount() { return totalAmount; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final RemoveItemResponse r = new RemoveItemResponse();
        public Builder message(String message) { r.message = message; return this; }
        public Builder items(List<CartItem> items) { r.items = items; return this; }
        public Builder totalAmount(Double totalAmount) { r.totalAmount = totalAmount; return this; }
        public RemoveItemResponse build() { return r; }
    }
}
