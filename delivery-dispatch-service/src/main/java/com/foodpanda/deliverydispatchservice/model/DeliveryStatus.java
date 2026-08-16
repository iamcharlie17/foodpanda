package com.foodpanda.deliverydispatchservice.model;

public enum DeliveryStatus {
    ASSIGNED,
    PICKED_UP,
    ON_THE_WAY,
    DELIVERED;

    public boolean canTransitionTo(DeliveryStatus next) {
        return switch (this) {
            case ASSIGNED   -> next == PICKED_UP;
            case PICKED_UP  -> next == ON_THE_WAY;
            case ON_THE_WAY -> next == DELIVERED;
            case DELIVERED  -> false;
        };
    }
}
