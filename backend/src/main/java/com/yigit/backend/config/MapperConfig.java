package com.yigit.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.yigit.backend.mapper.FlightMapper;
import org.mapstruct.factory.Mappers;

@Configuration
public class MapperConfig {
    @Bean
    public FlightMapper flightMapper() {
        return Mappers.getMapper(FlightMapper.class);
    }
} 