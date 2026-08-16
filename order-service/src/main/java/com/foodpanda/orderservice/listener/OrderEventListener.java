package com.foodpanda.orderservice.listener;

import com.foodpanda.orderservice.config.RabbitMQConfig;
import com.foodpanda.orderservice.dto.event.DeliveryEvent;
import com.foodpanda.orderservice.dto.event.PaymentEvent;
import com.foodpanda.orderservice.model.Order;
import com.foodpanda.orderservice.model.OrderStatus;
import com.foodpanda.orderservice.model.PaymentStatus;
import com.foodpanda.orderservice.model.StatusHistoryEntry;
import com.foodpanda.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    private final OrderRepository orderRepository;

    public OrderEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_PAYMENT_QUEUE)
    public void handlePaymentEvent(PaymentEvent event) {
        log.info("Received PaymentEvent for orderId={} with status={}", event.getOrderId(), event.getStatus());

        Optional<Order> orderOpt = orderRepository.findById(event.getOrderId());
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if ("SUCCESS".equals(event.getStatus())) {
                order.setPaymentStatus(PaymentStatus.PAID.name());
                order.setStatus(OrderStatus.ACCEPTED.name()); // Or PREPARING
                order.getStatusHistory().add(new StatusHistoryEntry(OrderStatus.ACCEPTED.name(), Instant.now()));
                orderRepository.save(order);
                log.info("Order {} updated to PAID and ACCEPTED", order.getId());
            } else if ("FAILED".equals(event.getStatus())) {
                order.setPaymentStatus(PaymentStatus.FAILED.name());
                orderRepository.save(order);
            }
        } else {
            log.warn("Order {} not found for PaymentEvent", event.getOrderId());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_DELIVERY_QUEUE)
    public void handleDeliveryEvent(DeliveryEvent event) {
        log.info("Received DeliveryEvent for orderId={} with status={}", event.getOrderId(), event.getStatus());

        Optional<Order> orderOpt = orderRepository.findById(event.getOrderId());
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            
            if ("ASSIGNED".equals(event.getStatus())) {
                order.setRiderId(event.getRiderId());
                orderRepository.save(order);
                log.info("Order {} assigned to rider {}", order.getId(), event.getRiderId());
            } else if ("DELIVERED".equals(event.getStatus())) {
                order.setStatus(OrderStatus.DELIVERED.name());
                order.getStatusHistory().add(new StatusHistoryEntry(OrderStatus.DELIVERED.name(), Instant.now()));
                orderRepository.save(order);
                log.info("Order {} marked as DELIVERED", order.getId());
            }
        } else {
            log.warn("Order {} not found for DeliveryEvent", event.getOrderId());
        }
    }
}
