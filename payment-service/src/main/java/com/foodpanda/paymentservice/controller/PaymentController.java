package com.foodpanda.paymentservice.controller;

import com.foodpanda.paymentservice.dto.*;
import com.foodpanda.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /** POST /api/payments */
    @PostMapping
    public ResponseEntity<PaymentResponse> initiatePayment(
            @RequestHeader(value = "Authorization", required = false) String token,
            @AuthenticationPrincipal String customerId,
            @Valid @RequestBody PaymentRequest request) {
        // Strip Bearer prefix if present
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiatePayment(customerId, request, token));
    }

    /** GET /api/payments/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    /** GET /api/payments/order/{orderId} */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentOrderResponse> getPaymentByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    /** POST /api/payments/{id}/refund */
    @PostMapping("/{id}/refund")
    public ResponseEntity<RefundResponse> refundPayment(
            @PathVariable String id,
            @Valid @RequestBody RefundRequest request) {
        return ResponseEntity.ok(paymentService.refundPayment(id, request));
    }
}
