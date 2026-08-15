package com.foodpanda.userservice.dto;

import com.foodpanda.userservice.model.Address;

import java.time.Instant;
import java.util.List;

public class UserProfileResponse {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private List<Address> addresses;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    private UserProfileResponse() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public List<Address> getAddresses() { return addresses; }
    public Boolean getIsActive() { return isActive; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final UserProfileResponse r = new UserProfileResponse();
        public Builder id(String id) { r.id = id; return this; }
        public Builder name(String name) { r.name = name; return this; }
        public Builder email(String email) { r.email = email; return this; }
        public Builder phone(String phone) { r.phone = phone; return this; }
        public Builder role(String role) { r.role = role; return this; }
        public Builder addresses(List<Address> addresses) { r.addresses = addresses; return this; }
        public Builder isActive(Boolean isActive) { r.isActive = isActive; return this; }
        public Builder createdAt(Instant createdAt) { r.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { r.updatedAt = updatedAt; return this; }
        public UserProfileResponse build() { return r; }
    }
}
