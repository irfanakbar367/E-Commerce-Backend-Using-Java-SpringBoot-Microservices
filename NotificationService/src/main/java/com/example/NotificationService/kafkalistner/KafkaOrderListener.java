package com.example.NotificationService.kafkalistner;

import com.example.Base_Domains.OrderNotifications.OrderEvent;
import com.example.NotificationService.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderListener {

    private final String topicName = "OrderTopic";
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(KafkaOrderListener.class);

    public KafkaOrderListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = topicName,
    groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFromOrder(OrderEvent orderEvent){
        logger.info(String.format("Order event => %s", orderEvent.toString()));

        emailService.mailSender("irfanakbar3676@gmail.com", "Mail Sender Test", orderEvent.getMessage());
    }
}
