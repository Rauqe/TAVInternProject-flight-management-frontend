package com.yigit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public ResponseEntity<?> health() {
        boolean dbUp = true;
        boolean kafkaUp = true;

        // DB kontrolü
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        } catch (Exception e) {
            dbUp = false;
        }

        // Kafka kontrolü
        try {
            kafkaTemplate.partitionsFor("flight-updates");
        } catch (Exception e) {
            kafkaUp = false;
        }

        return ResponseEntity.ok(new HealthStatusResponse(true, dbUp, kafkaUp));
    }

    static class HealthStatusResponse {
        private boolean api;
        private boolean database;
        private boolean kafka;

        public HealthStatusResponse(boolean api, boolean database, boolean kafka) {
            this.api = api;
            this.database = database;
            this.kafka = kafka;
        }
        public boolean isApi() { return api; }
        public boolean isDatabase() { return database; }
        public boolean isKafka() { return kafka; }
    }
} 