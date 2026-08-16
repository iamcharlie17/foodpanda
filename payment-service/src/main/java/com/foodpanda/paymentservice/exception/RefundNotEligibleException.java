package com.foodpanda.paymentservice.exception;

public class RefundNotEligibleException extends RuntimeException {

    public RefundNotEligibleException() {
        super("Payment is not in a refundable state");
    }
}
