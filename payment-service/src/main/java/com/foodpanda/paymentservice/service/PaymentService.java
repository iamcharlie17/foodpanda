package com.foodpanda.paymentservice.service;

import com.foodpanda.paymentservice.dto.*;
import com.foodpanda.paymentservice.exception.PaymentNotFoundException;
import com.foodpanda.paymentservice.exception.RefundNotEligibleException;
import com.foodpanda.paymentservice.model.Payment;
import com.foodpanda.paymentservice.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // ─────────────────────────────────────────────────────────────────────────

    public PaymentResponse initiatePayment(String customerId, PaymentRequest request) {
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .customerId(customerId)
                .amount(request.getAmount())
                .method(request.getMethod())
                .status("PENDING")
                .build();

        // Simulated gateway: cash on delivery stays PENDING until the order is
        // delivered; online methods are confirmed immediately.
        if (!"COD".equals(request.getMethod())) {
            payment.setStatus("SUCCESS");
            payment.setTransactionRef(generateReference(request.getMethod(), "TXN"));
        }

        Payment saved = paymentRepository.save(payment);
        log.info("Payment {} initiated for order={} status={}", saved.getId(), saved.getOrderId(), saved.getStatus());

        return toPaymentResponse(saved);
    }

    public PaymentResponse getPaymentById(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("No payment found with given id"));
        return toPaymentResponse(payment);
    }

    public PaymentOrderResponse getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("No payment found for given order"));

        return PaymentOrderResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .method(payment.getMethod())
                .transactionRef(payment.getTransactionRef())
                .build();
    }

    public RefundResponse refundPayment(String id, RefundRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("No payment found with given id"));

        if (!"SUCCESS".equals(payment.getStatus())) {
            throw new RefundNotEligibleException();
        }

        payment.setStatus("REFUNDED");
        payment.setRefundRef(generateReference(payment.getMethod(), "RFD"));
        Payment saved = paymentRepository.save(payment);
        log.info("Payment {} refunded, reason={}", saved.getId(), request.getReason());

        return RefundResponse.builder()
                .id(saved.getId())
                .orderId(saved.getOrderId())
                .status(saved.getStatus())
                .amount(saved.getAmount())
                .transactionRef(saved.getTransactionRef())
                .refundRef(saved.getRefundRef())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────

    private String generateReference(String method, String kind) {
        String prefix = "MOBILE_BANKING".equals(method) ? "bKash-" + kind : kind;
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        return prefix + "-" + random;
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .method(payment.getMethod())
                .transactionRef(payment.getTransactionRef())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
