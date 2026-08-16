package com.foodpanda.paymentservice.dto.event;

public class PaymentEvent {
    private String orderId;
    private String paymentId;
    private String customerId;
    private double amount;
    private String status;

    public PaymentEvent() {}

    public PaymentEvent(String orderId, String paymentId, String customerId, double amount, String status) {
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
