package com.foodpanda.notificationservice.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String userId;

    private String type;

    private String channel;

    private String message;

    @Field("isRead")
    private Boolean isRead = false;

    @CreatedDate
    private Instant createdAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Notification() {}

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
        private final Notification n = new Notification();

        public Builder userId(String userId) { n.userId = userId; return this; }
        public Builder type(String type) { n.type = type; return this; }
        public Builder channel(String channel) { n.channel = channel; return this; }
        public Builder message(String message) { n.message = message; return this; }
        public Builder isRead(Boolean isRead) { n.isRead = isRead; return this; }
        public Notification build() { return n; }
    }
}
