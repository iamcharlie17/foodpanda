package com.foodpanda.userservice.dto;

public class LoginResponse {

    private String accessToken;
    private long expiresIn;
    private UserSummary user;

    private LoginResponse() {}

    public String getAccessToken() { return accessToken; }
    public long getExpiresIn() { return expiresIn; }
    public UserSummary getUser() { return user; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final LoginResponse r = new LoginResponse();
        public Builder accessToken(String t) { r.accessToken = t; return this; }
        public Builder expiresIn(long e) { r.expiresIn = e; return this; }
        public Builder user(UserSummary u) { r.user = u; return this; }
        public LoginResponse build() { return r; }
    }

    // ── Nested summary ────────────────────────────────────────────────────────

    public static class UserSummary {
        private String id;
        private String name;
        private String role;

        private UserSummary() {}

        public String getId() { return id; }
        public String getName() { return name; }
        public String getRole() { return role; }

        public static SummaryBuilder builder() { return new SummaryBuilder(); }

        public static class SummaryBuilder {
            private final UserSummary s = new UserSummary();
            public SummaryBuilder id(String id) { s.id = id; return this; }
            public SummaryBuilder name(String name) { s.name = name; return this; }
            public SummaryBuilder role(String role) { s.role = role; return this; }
            public UserSummary build() { return s; }
        }
    }
}
