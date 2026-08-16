package com.foodpanda.orderservice.model;

/**
 * Valid order status values and allowed transitions.
 *
 * Flow: PLACED → ACCEPTED → PREPARING → READY → PICKED_UP → DELIVERED
 *                    ↓
 *                REJECTED
 */
public enum OrderStatus {
    PLACED,
    ACCEPTED,
    PREPARING,
    READY,
    PICKED_UP,
    DELIVERED,
    REJECTED;

    /**
     * Returns true if transitioning from this status to {@code next} is allowed.
     */
    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case PLACED    -> next == ACCEPTED || next == REJECTED;
            case ACCEPTED  -> next == PREPARING;
            case PREPARING -> next == READY;
            case READY     -> next == PICKED_UP;
            case PICKED_UP -> next == DELIVERED;
            // Terminal states — no further transitions
            case DELIVERED, REJECTED -> false;
        };
    }
}
