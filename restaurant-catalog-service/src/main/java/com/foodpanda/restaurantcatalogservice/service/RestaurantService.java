package com.foodpanda.restaurantcatalogservice.service;

import com.foodpanda.restaurantcatalogservice.dto.*;
import com.foodpanda.restaurantcatalogservice.exception.MenuItemNotFoundException;
import com.foodpanda.restaurantcatalogservice.exception.RestaurantNotFoundException;
import com.foodpanda.restaurantcatalogservice.exception.UnauthorizedOperationException;
import com.foodpanda.restaurantcatalogservice.model.Address;
import com.foodpanda.restaurantcatalogservice.model.MenuItem;
import com.foodpanda.restaurantcatalogservice.model.OperatingHours;
import com.foodpanda.restaurantcatalogservice.model.Restaurant;
import com.foodpanda.restaurantcatalogservice.repository.MenuItemRepository;
import com.foodpanda.restaurantcatalogservice.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Restaurants
    // ─────────────────────────────────────────────────────────────────────────

    public RestaurantResponse createRestaurant(String ownerId, CreateRestaurantRequest request) {
        Address address = null;
        if (request.getAddress() != null) {
            AddressDto a = request.getAddress();
            address = new Address(a.getStreet(), a.getCity(), a.getLat(), a.getLng());
        }

        OperatingHours hours = null;
        if (request.getOperatingHours() != null) {
            hours = new OperatingHours(
                    request.getOperatingHours().getOpen(),
                    request.getOperatingHours().getClose()
            );
        }

        Restaurant restaurant = Restaurant.builder()
                .ownerId(ownerId)
                .name(request.getName())
                .description(request.getDescription())
                .cuisine(request.getCuisine())
                .address(address)
                .operatingHours(hours)
                .rating(0.0)
                .isOpen(true)
                .isApproved(false)
                .build();

        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Created restaurant id={} ownerId={}", saved.getId(), ownerId);

        return toRestaurantResponse(saved);
    }

    public PagedRestaurantResponse listRestaurants(String city, String cuisine, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        Page<Restaurant> resultPage;

        boolean hasCity = city != null && !city.isBlank();
        boolean hasCuisine = cuisine != null && !cuisine.isBlank();

        if (hasCity && hasCuisine) {
            resultPage = restaurantRepository.findByCityAndCuisine(
                    city,
                    cuisine,
                    pageable
            );
        } else if (hasCity) {
            resultPage = restaurantRepository.findByCity(
                    city,
                    pageable
            );
        } else if (hasCuisine) {
            resultPage = restaurantRepository.findByCuisine(
                    cuisine,
                    pageable
            );
        } else {
            resultPage = restaurantRepository.findAll(pageable);
        }

        List<RestaurantSummaryResponse> content = resultPage.getContent().stream()
                .map(this::toSummaryResponse)
                .toList();

        return new PagedRestaurantResponse(
                content,
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getTotalElements(),
                resultPage.getTotalPages()
        );
    }

    public RestaurantResponse getRestaurant(String restaurantId) {
        Restaurant restaurant = findRestaurant(restaurantId);
        return toRestaurantResponse(restaurant);
    }

    public UpdateRestaurantResponse updateRestaurant(String ownerId,
                                                     String restaurantId,
                                                     UpdateRestaurantRequest request) {
        Restaurant restaurant = findRestaurant(restaurantId);
        verifyOwner(restaurant, ownerId);

        if (request.getDescription() != null) {
            restaurant.setDescription(request.getDescription());
        }
        if (request.getIsOpen() != null) {
            restaurant.setIsOpen(request.getIsOpen());
        }
        if (request.getOperatingHours() != null) {
            restaurant.setOperatingHours(new OperatingHours(
                    request.getOperatingHours().getOpen(),
                    request.getOperatingHours().getClose()
            ));
        }

        Restaurant saved = restaurantRepository.save(restaurant);

        OperatingHoursDto hoursDto = null;
        if (saved.getOperatingHours() != null) {
            hoursDto = new OperatingHoursDto(
                    saved.getOperatingHours().getOpen(),
                    saved.getOperatingHours().getClose()
            );
        }

        return UpdateRestaurantResponse.builder()
                .id(saved.getId())
                .description(saved.getDescription())
                .isOpen(saved.getIsOpen())
                .operatingHours(hoursDto)
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Menu Items
    // ─────────────────────────────────────────────────────────────────────────

    public MenuItemResponse addMenuItem(String ownerId,
                                        String restaurantId,
                                        CreateMenuItemRequest request) {
        Restaurant restaurant = findRestaurant(restaurantId);
        // Only verify ownership when the caller is authenticated (ownerId comes from JWT sub)
        if (ownerId != null) {
            verifyOwner(restaurant, ownerId);
        }

        MenuItem item = MenuItem.builder()
                .restaurantId(restaurantId)
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true)
                .imageUrl(request.getImageUrl())
                .build();

        log.info("Saving menu item for restaurantId={} ownerId={}", restaurantId, ownerId);
        MenuItem saved = menuItemRepository.save(item);
        log.info("Saved menu item id={} to restaurantId={}", saved.getId(), restaurantId);

        return toMenuItemResponse(saved);
    }

    public List<MenuItemResponse> listMenuItems(String restaurantId, String category) {
        // Verify restaurant exists
        findRestaurant(restaurantId);

        List<MenuItem> items = (category != null && !category.isBlank())
                ? menuItemRepository.findByRestaurantIdAndCategory(restaurantId, category)
                : menuItemRepository.findByRestaurantId(restaurantId);

        return items.stream().map(this::toMenuItemResponse).toList();
    }

    public UpdateMenuItemResponse updateMenuItem(String ownerId,
                                                 String restaurantId,
                                                 String itemId,
                                                 UpdateMenuItemRequest request) {
        Restaurant restaurant = findRestaurant(restaurantId);
        verifyOwner(restaurant, ownerId);

        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new MenuItemNotFoundException(itemId));

        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            item.setCategory(request.getCategory());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }
        if (request.getIsAvailable() != null) {
            item.setIsAvailable(request.getIsAvailable());
        }
        if (request.getImageUrl() != null) {
            item.setImageUrl(request.getImageUrl());
        }

        MenuItem saved = menuItemRepository.save(item);

        return UpdateMenuItemResponse.builder()
                .id(saved.getId())
                .price(saved.getPrice())
                .isAvailable(saved.getIsAvailable())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public void deleteMenuItem(String ownerId, String restaurantId, String itemId) {
        Restaurant restaurant = findRestaurant(restaurantId);
        verifyOwner(restaurant, ownerId);

        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new MenuItemNotFoundException(itemId));

        menuItemRepository.delete(item);
        log.info("Deleted menu item id={} from restaurantId={}", itemId, restaurantId);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private Restaurant findRestaurant(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
    }

    private void verifyOwner(Restaurant restaurant, String ownerId) {
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new UnauthorizedOperationException();
        }
    }

    private RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        AddressDto addressDto = null;
        if (restaurant.getAddress() != null) {
            Address a = restaurant.getAddress();
            addressDto = new AddressDto(a.getStreet(), a.getCity(), a.getLat(), a.getLng());
        }

        OperatingHoursDto hoursDto = null;
        if (restaurant.getOperatingHours() != null) {
            hoursDto = new OperatingHoursDto(
                    restaurant.getOperatingHours().getOpen(),
                    restaurant.getOperatingHours().getClose()
            );
        }

        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .ownerId(restaurant.getOwnerId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .cuisine(restaurant.getCuisine())
                .address(addressDto)
                .operatingHours(hoursDto)
                .rating(restaurant.getRating())
                .isOpen(restaurant.getIsOpen())
                .isApproved(restaurant.getIsApproved())
                .createdAt(restaurant.getCreatedAt())
                .updatedAt(restaurant.getUpdatedAt())
                .build();
    }

    private RestaurantSummaryResponse toSummaryResponse(Restaurant restaurant) {
        String city = (restaurant.getAddress() != null) ? restaurant.getAddress().getCity() : null;

        return RestaurantSummaryResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .cuisine(restaurant.getCuisine())
                .city(city)
                .rating(restaurant.getRating())
                .isOpen(restaurant.getIsOpen())
                .build();
    }

    private MenuItemResponse toMenuItemResponse(MenuItem item) {
        return MenuItemResponse.builder()
                .id(item.getId())
                .restaurantId(item.getRestaurantId())
                .name(item.getName())
                .description(item.getDescription())
                .category(item.getCategory())
                .price(item.getPrice())
                .isAvailable(item.getIsAvailable())
                .imageUrl(item.getImageUrl())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
