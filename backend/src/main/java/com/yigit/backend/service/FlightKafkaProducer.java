package com.yigit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class FlightKafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendFlightUpdate(String flightJson) {
        kafkaTemplate.send("flight-updates", flightJson);
        System.out.println("[FlightKafkaProducer] Sent flight update to Kafka topic: flight-updates");
    }
} 