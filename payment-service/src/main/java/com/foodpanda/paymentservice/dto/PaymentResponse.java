package com.foodpanda.paymentservice.dto;

import java.time.Instant;

public class PaymentResponse {

    private String id;
    private String orderId;
    private String customerId;
    private Double amount;
    private String status;
    private String method;
    private String transactionRef;
    private Instant createdAt;
    private Instant updatedAt;

    private PaymentResponse() {}

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public Double getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getMethod() { return method; }
    public String getTransactionRef() { return transactionRef; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final PaymentResponse r = new PaymentResponse();
        public Builder id(String id) { r.id = id; return this; }
        public Builder orderId(String orderId) { r.orderId = orderId; return this; }
        public Builder customerId(String customerId) { r.customerId = customerId; return this; }
        public Builder amount(Double amount) { r.amount = amount; return this; }
        public Builder status(String status) { r.status = status; return this; }
        public Builder method(String method) { r.method = method; return this; }
        public Builder transactionRef(String transactionRef) { r.transactionRef = transactionRef; return this; }
        public Builder createdAt(Instant createdAt) { r.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { r.updatedAt = updatedAt; return this; }
        public PaymentResponse build() { return r; }
    }
}
