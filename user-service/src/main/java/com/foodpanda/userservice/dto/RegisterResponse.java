package com.foodpanda.userservice.dto;

import java.time.Instant;

public class RegisterResponse {

    private String id;
    private String name;
    private String email;
    private String role;
    private Instant createdAt;

    private RegisterResponse() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Instant getCreatedAt() { return createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final RegisterResponse r = new RegisterResponse();
        public Builder id(String id) { r.id = id; return this; }
        public Builder name(String name) { r.name = name; return this; }
        public Builder email(String email) { r.email = email; return this; }
        public Builder role(String role) { r.role = role; return this; }
        public Builder createdAt(Instant createdAt) { r.createdAt = createdAt; return this; }
        public RegisterResponse build() { return r; }
    }
}
