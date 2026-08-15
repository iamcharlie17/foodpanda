package com.foodpanda.paymentservice.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    @Indexed
    private String orderId;

    private String customerId;

    private Double amount;

    private String status;

    private String method;

    private String transactionRef;

    private String refundRef;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Payment() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public String getRefundRef() { return refundRef; }
    public void setRefundRef(String refundRef) { this.refundRef = refundRef; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Payment payment = new Payment();

        public Builder id(String id) { payment.id = id; return this; }
        public Builder orderId(String orderId) { payment.orderId = orderId; return this; }
        public Builder customerId(String customerId) { payment.customerId = customerId; return this; }
        public Builder amount(Double amount) { payment.amount = amount; return this; }
        public Builder status(String status) { payment.status = status; return this; }
        public Builder method(String method) { payment.method = method; return this; }
        public Builder transactionRef(String transactionRef) { payment.transactionRef = transactionRef; return this; }
        public Builder refundRef(String refundRef) { payment.refundRef = refundRef; return this; }
        public Payment build() { return payment; }
    }
}
