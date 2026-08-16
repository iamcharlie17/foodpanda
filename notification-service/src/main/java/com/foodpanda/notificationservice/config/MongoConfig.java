package com.foodpanda.notificationservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Enables @CreatedDate population on MongoDB documents.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
}
