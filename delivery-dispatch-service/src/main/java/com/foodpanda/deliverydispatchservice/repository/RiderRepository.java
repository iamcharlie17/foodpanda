package com.foodpanda.deliverydispatchservice.repository;

import com.foodpanda.deliverydispatchservice.model.Rider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends MongoRepository<Rider, String> {

    Optional<Rider> findByUserId(String userId);

    Optional<Rider> findFirstByIsAvailableTrue();
}
