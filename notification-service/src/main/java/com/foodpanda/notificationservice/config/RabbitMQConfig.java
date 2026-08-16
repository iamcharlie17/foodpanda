package com.foodpanda.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // External exchanges we need to bind to
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String DELIVERY_EXCHANGE = "delivery.exchange";

    // Queues for this service
    public static final String NOTIFICATION_ORDER_QUEUE = "notification.order.queue";
    public static final String NOTIFICATION_PAYMENT_QUEUE = "notification.payment.queue";
    public static final String NOTIFICATION_DELIVERY_QUEUE = "notification.delivery.queue";

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
    public Queue notificationOrderQueue() {
        return new Queue(NOTIFICATION_ORDER_QUEUE);
    }

    @Bean
    public Queue notificationPaymentQueue() {
        return new Queue(NOTIFICATION_PAYMENT_QUEUE);
    }

    @Bean
    public Queue notificationDeliveryQueue() {
        return new Queue(NOTIFICATION_DELIVERY_QUEUE);
    }

    @Bean
    public Binding notificationOrderBinding(Queue notificationOrderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(notificationOrderQueue).to(orderExchange).with("order.placed");
    }

    @Bean
    public Binding notificationPaymentBinding(Queue notificationPaymentQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(notificationPaymentQueue).to(paymentExchange).with("payment.completed");
    }

    @Bean
    public Binding notificationDeliveryBinding(Queue notificationDeliveryQueue, TopicExchange deliveryExchange) {
        return BindingBuilder.bind(notificationDeliveryQueue).to(deliveryExchange).with("delivery.updated");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
