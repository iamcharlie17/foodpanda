package com.foodpanda.notificationservice.dto;

/**
 * Slim response for PATCH /api/notifications/{id}/read — matches spec shape exactly.
 */
public class MarkReadResponse {

    private String id;
    private Boolean isRead;

    public MarkReadResponse() {}

    public MarkReadResponse(String id, Boolean isRead) {
        this.id = id;
        this.isRead = isRead;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
}
