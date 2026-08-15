package com.foodpanda.notificationservice.dto;

import java.time.Instant;

/**
 * Full notification response — used for the internal create (201) endpoint.
 */
public class NotificationResponse {

    private String id;
    private String userId;
    private String type;
    private String channel;
    private String message;
    private Boolean isRead;
    private Instant createdAt;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

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
        private final NotificationResponse r = new NotificationResponse();

        public Builder id(String id) { r.id = id; return this; }
        public Builder userId(String userId) { r.userId = userId; return this; }
        public Builder type(String type) { r.type = type; return this; }
        public Builder channel(String channel) { r.channel = channel; return this; }
        public Builder message(String message) { r.message = message; return this; }
        public Builder isRead(Boolean isRead) { r.isRead = isRead; return this; }
        public Builder createdAt(Instant createdAt) { r.createdAt = createdAt; return this; }
        public NotificationResponse build() { return r; }
    }
}
