package com.foodpanda.restaurantcatalogservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Enables @CreatedDate and @LastModifiedDate population on MongoDB documents.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
}
