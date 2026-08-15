package com.foodpanda.userservice.service;

import com.foodpanda.userservice.dto.*;
import com.foodpanda.userservice.exception.EmailAlreadyExistsException;
import com.foodpanda.userservice.exception.InvalidCredentialsException;
import com.foodpanda.userservice.exception.UserNotFoundException;
import com.foodpanda.userservice.model.Address;
import com.foodpanda.userservice.model.User;
import com.foodpanda.userservice.repository.UserRepository;
import com.foodpanda.userservice.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Auth
    // ─────────────────────────────────────────────────────────────────────────

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .active(true)
                .build();

        User saved = userRepository.save(user);
        log.info("Registered new user id={} email={}", saved.getId(), saved.getEmail());

        return RegisterResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        return LoginResponse.builder()
                .accessToken(token)
                .expiresIn(jwtUtil.getExpiresInSeconds())
                .user(LoginResponse.UserSummary.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .role(user.getRole())
                        .build())
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Profile
    // ─────────────────────────────────────────────────────────────────────────

    public UserProfileResponse getProfile(String userId) {
        User user = findActiveUser(userId);

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .addresses(user.getAddresses())
                .isActive(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public UpdateProfileResponse updateProfile(String userId, UpdateProfileRequest request) {
        User user = findActiveUser(userId);

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            user.setPhone(request.getPhone());
        }

        User saved = userRepository.save(user);

        return UpdateProfileResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .phone(saved.getPhone())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public void deactivateAccount(String userId) {
        User user = findActiveUser(userId);
        user.setActive(false);
        userRepository.save(user);
        log.info("Deactivated user id={}", userId);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Addresses
    // ─────────────────────────────────────────────────────────────────────────

    public List<Address> addAddress(String userId, AddressRequest request) {
        User user = findActiveUser(userId);

        Address address = new Address(
                request.getLabel(),
                request.getStreet(),
                request.getCity(),
                request.getLat(),
                request.getLng(),
                request.getIsDefault()
        );

        // If new address is default, clear default flag from all others
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            user.getAddresses().forEach(a -> a.setIsDefault(false));
        }

        user.getAddresses().add(address);
        User saved = userRepository.save(user);

        return saved.getAddresses();
    }

    public List<Address> listAddresses(String userId) {
        User user = findActiveUser(userId);
        return user.getAddresses();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private User findActiveUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }
}
