package com.foodpanda.userservice.controller;

import com.foodpanda.userservice.dto.*;
import com.foodpanda.userservice.model.Address;
import com.foodpanda.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ── Auth (public) ─────────────────────────────────────────────────────────

    /** POST /api/users/auth/register */
    @PostMapping("/auth/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    /** POST /api/users/auth/login */
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    // ── Profile (authenticated) ───────────────────────────────────────────────

    /** GET /api/users/me */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    /** PUT /api/users/me */
    @PutMapping("/me")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @AuthenticationPrincipal String userId,
            @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    /** DELETE /api/users/me  — soft deactivate */
    @DeleteMapping("/me")
    public ResponseEntity<Map<String, String>> deactivateAccount(
            @AuthenticationPrincipal String userId) {
        userService.deactivateAccount(userId);
        return ResponseEntity.ok(Map.of("message", "Account deactivated successfully"));
    }

    // ── Addresses (authenticated) ─────────────────────────────────────────────

    /** POST /api/users/me/addresses */
    @PostMapping("/me/addresses")
    public ResponseEntity<Map<String, Object>> addAddress(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody AddressRequest request) {
        List<Address> addresses = userService.addAddress(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message",   "Address added successfully",
                "addresses", addresses
        ));
    }

    /** GET /api/users/me/addresses */
    @GetMapping("/me/addresses")
    public ResponseEntity<List<Address>> listAddresses(
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(userService.listAddresses(userId));
    }
}
