package com.foodpanda.orderservice.model;

/**
 * A single item within an order.
 */
public class OrderItem {

    private String menuItemId;
    private String name;
    private Double price;
    private Integer quantity;

    public OrderItem() {}

    public OrderItem(String menuItemId, String name, Double price, Integer quantity) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getMenuItemId() { return menuItemId; }
    public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
