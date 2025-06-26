package com.yigit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public Map<String, Object> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("api", true);
        try {
            // DB health
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            status.put("database", true);
        } catch (Exception e) {
            status.put("database", false);
        }
        try {
            // Kafka health
            kafkaTemplate.partitionsFor("flight-updates");
            status.put("kafka", true);
        } catch (Exception e) {
            status.put("kafka", false);
        }
        try {
            redisTemplate.opsForValue().set("health_check", "ok");
            String val = (String) redisTemplate.opsForValue().get("health_check");
            status.put("redis", "ok".equals(val));
        } catch (Exception e) {
            status.put("redis", false);
        }
        return status;
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