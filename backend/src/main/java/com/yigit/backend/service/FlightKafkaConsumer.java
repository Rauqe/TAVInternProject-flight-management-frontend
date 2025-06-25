package com.yigit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class FlightKafkaConsumer {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "flight-updates", groupId = "ws-group")
    public void listen(String message) {
        messagingTemplate.convertAndSend("/topic/flights", message);
    }
} 