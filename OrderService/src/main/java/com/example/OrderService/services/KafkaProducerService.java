package com.example.OrderService.services;

import com.example.Base_Domains.OrderNotifications.OrderEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private NewTopic newTopic;
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public KafkaProducerService(NewTopic newTopic, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.newTopic = newTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(OrderEvent orderEvent) {
        logger.info(String.format("Order event => %s", orderEvent.toString()));

        Message<OrderEvent> message = MessageBuilder
                .withPayload(orderEvent)
                .setHeader(KafkaHeaders.TOPIC, newTopic.name())
                .build();

        kafkaTemplate.send(message);
    }

}
