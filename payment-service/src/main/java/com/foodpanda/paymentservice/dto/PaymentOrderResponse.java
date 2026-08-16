package com.foodpanda.paymentservice.dto;

public class PaymentOrderResponse {

    private String id;
    private String orderId;
    private Double amount;
    private String status;
    private String method;
    private String transactionRef;

    private PaymentOrderResponse() {}

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public Double getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getMethod() { return method; }
    public String getTransactionRef() { return transactionRef; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final PaymentOrderResponse r = new PaymentOrderResponse();
        public Builder id(String id) { r.id = id; return this; }
        public Builder orderId(String orderId) { r.orderId = orderId; return this; }
        public Builder amount(Double amount) { r.amount = amount; return this; }
        public Builder status(String status) { r.status = status; return this; }
        public Builder method(String method) { r.method = method; return this; }
        public Builder transactionRef(String transactionRef) { r.transactionRef = transactionRef; return this; }
        public PaymentOrderResponse build() { return r; }
    }
}
