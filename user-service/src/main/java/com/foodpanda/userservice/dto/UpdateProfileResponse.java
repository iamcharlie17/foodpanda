package com.foodpanda.userservice.dto;

import java.time.Instant;

public class UpdateProfileResponse {

    private String id;
    private String name;
    private String phone;
    private Instant updatedAt;

    private UpdateProfileResponse() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public Instant getUpdatedAt() { return updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final UpdateProfileResponse r = new UpdateProfileResponse();
        public Builder id(String id) { r.id = id; return this; }
        public Builder name(String name) { r.name = name; return this; }
        public Builder phone(String phone) { r.phone = phone; return this; }
        public Builder updatedAt(Instant updatedAt) { r.updatedAt = updatedAt; return this; }
        public UpdateProfileResponse build() { return r; }
    }
}
