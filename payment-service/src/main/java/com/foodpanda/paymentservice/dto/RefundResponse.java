package com.foodpanda.paymentservice.dto;

import java.time.Instant;

public class RefundResponse {

    private String id;
    private String orderId;
    private String status;
    private Double amount;
    private String transactionRef;
    private String refundRef;
    private Instant updatedAt;

    private RefundResponse() {}

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public Double getAmount() { return amount; }
    public String getTransactionRef() { return transactionRef; }
    public String getRefundRef() { return refundRef; }
    public Instant getUpdatedAt() { return updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final RefundResponse r = new RefundResponse();
        public Builder id(String id) { r.id = id; return this; }
        public Builder orderId(String orderId) { r.orderId = orderId; return this; }
        public Builder status(String status) { r.status = status; return this; }
        public Builder amount(Double amount) { r.amount = amount; return this; }
        public Builder transactionRef(String transactionRef) { r.transactionRef = transactionRef; return this; }
        public Builder refundRef(String refundRef) { r.refundRef = refundRef; return this; }
        public Builder updatedAt(Instant updatedAt) { r.updatedAt = updatedAt; return this; }
        public RefundResponse build() { return r; }
    }
}
