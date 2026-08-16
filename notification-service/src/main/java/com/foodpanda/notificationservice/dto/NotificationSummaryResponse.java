package com.foodpanda.notificationservice.dto;

import java.time.Instant;

/**
 * Slim notification item used inside the paginated list response for user-facing GET.
 * Does NOT include userId — the authenticated user already knows their own ID.
 */
public class NotificationSummaryResponse {

    private String id;
    private String type;
    private String channel;
    private String message;
    private Boolean isRead;
    private Instant createdAt;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final NotificationSummaryResponse r = new NotificationSummaryResponse();

        public Builder id(String id) { r.id = id; return this; }
        public Builder type(String type) { r.type = type; return this; }
        public Builder channel(String channel) { r.channel = channel; return this; }
        public Builder message(String message) { r.message = message; return this; }
        public Builder isRead(Boolean isRead) { r.isRead = isRead; return this; }
        public Builder createdAt(Instant createdAt) { r.createdAt = createdAt; return this; }
        public NotificationSummaryResponse build() { return r; }
    }
}
