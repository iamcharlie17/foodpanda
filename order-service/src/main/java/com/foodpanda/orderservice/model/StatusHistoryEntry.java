package com.foodpanda.orderservice.model;

import java.time.Instant;

/**
 * A single entry in the order's status audit trail.
 */
public class StatusHistoryEntry {

    private String status;
    private Instant timestamp;

    public StatusHistoryEntry() {}

    public StatusHistoryEntry(String status, Instant timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
