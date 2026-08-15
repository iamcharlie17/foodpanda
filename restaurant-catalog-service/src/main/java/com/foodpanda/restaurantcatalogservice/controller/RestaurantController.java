package com.foodpanda.restaurantcatalogservice.controller;

import com.foodpanda.restaurantcatalogservice.dto.*;
import com.foodpanda.restaurantcatalogservice.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // ── Restaurants ───────────────────────────────────────────────────────────

    /** POST /api/restaurants */
    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @AuthenticationPrincipal String ownerId,
            @Valid @RequestBody CreateRestaurantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.createRestaurant(ownerId, request));
    }

    /** GET /api/restaurants?city=&cuisine=&page=0&size=10 */
    @GetMapping
    public ResponseEntity<PagedRestaurantResponse> listRestaurants(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String cuisine,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(restaurantService.listRestaurants(city, cuisine, page, size));
    }

    /** GET /api/restaurants/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurant(
            @PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getRestaurant(id));
    }

    /** PUT /api/restaurants/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<UpdateRestaurantResponse> updateRestaurant(
            @AuthenticationPrincipal String ownerId,
            @PathVariable String id,
            @RequestBody UpdateRestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(ownerId, id, request));
    }

    // ── Menu Items ────────────────────────────────────────────────────────────

    /** POST /api/restaurants/{id}/menu-items */
    @PostMapping("/{id}/menu-items")
    public ResponseEntity<MenuItemResponse> addMenuItem(
            @AuthenticationPrincipal String ownerId,
            @PathVariable String id,
            @Valid @RequestBody CreateMenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.addMenuItem(ownerId, id, request));
    }

    /** GET /api/restaurants/{id}/menu-items?category= */
    @GetMapping("/{id}/menu-items")
    public ResponseEntity<List<MenuItemResponse>> listMenuItems(
            @PathVariable String id,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(restaurantService.listMenuItems(id, category));
    }

    /** PUT /api/restaurants/{id}/menu-items/{itemId} */
    @PutMapping("/{id}/menu-items/{itemId}")
    public ResponseEntity<UpdateMenuItemResponse> updateMenuItem(
            @AuthenticationPrincipal String ownerId,
            @PathVariable String id,
            @PathVariable String itemId,
            @RequestBody UpdateMenuItemRequest request) {
        return ResponseEntity.ok(restaurantService.updateMenuItem(ownerId, id, itemId, request));
    }

    /** DELETE /api/restaurants/{id}/menu-items/{itemId} */
    @DeleteMapping("/{id}/menu-items/{itemId}")
    public ResponseEntity<Map<String, String>> deleteMenuItem(
            @AuthenticationPrincipal String ownerId,
            @PathVariable String id,
            @PathVariable String itemId) {
        restaurantService.deleteMenuItem(ownerId, id, itemId);
        return ResponseEntity.ok(Map.of("message", "Menu item deleted successfully"));
    }
}
