package com.foodpanda.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateNotificationRequest {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "type is required")
    private String type;

    @NotBlank(message = "channel is required")
    private String channel;

    @NotBlank(message = "message is required")
    private String message;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
