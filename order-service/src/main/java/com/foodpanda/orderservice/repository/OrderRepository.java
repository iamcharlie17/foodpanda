package com.foodpanda.orderservice.repository;

import com.foodpanda.orderservice.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    /** All orders belonging to a customer, paginated and optionally filtered by status */
    Page<Order> findByCustomerId(String customerId, Pageable pageable);

    Page<Order> findByCustomerIdAndStatus(String customerId, String status, Pageable pageable);

    /** All orders for a restaurant, optionally filtered by status */
    List<Order> findByRestaurantId(String restaurantId);

    List<Order> findByRestaurantIdAndStatus(String restaurantId, String status);
}
