package com.foodpanda.cartservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DifferentRestaurantException.class)
    public ResponseEntity<Map<String, String>> handleDifferentRestaurant(DifferentRestaurantException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error",   "DIFFERENT_RESTAURANT_IN_CART",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartItemNotFound(CartItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error",   "CART_ITEM_NOT_FOUND",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(Map.of(
                "error",  "VALIDATION_ERROR",
                "fields", errors
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error",   "INTERNAL_ERROR",
                "message", ex.getMessage() != null ? ex.getMessage() : "Unexpected error"
        ));
    }
}
