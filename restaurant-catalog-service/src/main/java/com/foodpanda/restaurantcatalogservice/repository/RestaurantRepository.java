package com.foodpanda.restaurantcatalogservice.repository;

import com.foodpanda.restaurantcatalogservice.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {

    @Query("{ 'address.city': { $regex: ?0, $options: 'i' } }")
    Page<Restaurant> findByCity(String city, Pageable pageable);

    @Query("{ 'cuisine': { $regex: ?0, $options: 'i' } }")
    Page<Restaurant> findByCuisine(String cuisine, Pageable pageable);

    @Query("{ 'address.city': { $regex: ?0, $options: 'i' }, " +
            "'cuisine': { $regex: ?1, $options: 'i' } }")
    Page<Restaurant> findByCityAndCuisine(
            String city,
            String cuisine,
            Pageable pageable
    );
}