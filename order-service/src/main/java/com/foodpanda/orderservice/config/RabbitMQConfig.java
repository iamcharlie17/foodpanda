package com.foodpanda.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    
    // External exchanges we need to bind to
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String DELIVERY_EXCHANGE = "delivery.exchange";

    // Queues for this service
    public static final String ORDER_PAYMENT_QUEUE = "order.payment.queue";
    public static final String ORDER_DELIVERY_QUEUE = "order.delivery.queue";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public TopicExchange deliveryExchange() {
        return new TopicExchange(DELIVERY_EXCHANGE);
    }

    @Bean
    public Queue orderPaymentQueue() {
        return new Queue(ORDER_PAYMENT_QUEUE);
    }

    @Bean
    public Queue orderDeliveryQueue() {
        return new Queue(ORDER_DELIVERY_QUEUE);
    }

    @Bean
    public Binding paymentBinding(Queue orderPaymentQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(orderPaymentQueue).to(paymentExchange).with("payment.completed");
    }

    @Bean
    public Binding deliveryBinding(Queue orderDeliveryQueue, TopicExchange deliveryExchange) {
        return BindingBuilder.bind(orderDeliveryQueue).to(deliveryExchange).with("delivery.updated");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
