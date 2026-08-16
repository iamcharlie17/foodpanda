package com.foodpanda.notificationservice.listener;

import com.foodpanda.notificationservice.config.RabbitMQConfig;
import com.foodpanda.notificationservice.dto.event.DeliveryEvent;
import com.foodpanda.notificationservice.dto.event.OrderEvent;
import com.foodpanda.notificationservice.dto.event.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_ORDER_QUEUE)
    public void handleOrderEvent(OrderEvent event) {
        log.info("🔔 [NOTIFICATION] Order {} placed successfully. Status: {}. Amount: {}", 
                event.getOrderId(), event.getStatus(), event.getTotalAmount());
        // Here you would integrate with an email/SMS provider (e.g., SendGrid, Twilio)
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_PAYMENT_QUEUE)
    public void handlePaymentEvent(PaymentEvent event) {
        if ("SUCCESS".equals(event.getStatus())) {
            log.info("🔔 [NOTIFICATION] Payment successful for Order {}. Amount paid: {}", 
                    event.getOrderId(), event.getAmount());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_DELIVERY_QUEUE)
    public void handleDeliveryEvent(DeliveryEvent event) {
        if ("ASSIGNED".equals(event.getStatus())) {
            log.info("🔔 [NOTIFICATION] A rider has been assigned to your Order {}", event.getOrderId());
        } else if ("DELIVERED".equals(event.getStatus())) {
            log.info("🔔 [NOTIFICATION] Your Order {} has been delivered! Enjoy your meal.", event.getOrderId());
        } else {
            log.info("🔔 [NOTIFICATION] Order {} delivery status updated to: {}", 
                    event.getOrderId(), event.getStatus());
        }
    }
}
