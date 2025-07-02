package com.yigit.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponseDTO {
    private Long id;
    private String flightNumber;
    private String airlineCode;
    private String airlineName;
    private String aircraftTypeCode;
    private String aircraftTypeName;
    private String originCode;
    private String originName;
    private String destinationCode;
    private String destinationName;
    private LocalDate flightDate;
    private LocalTime std;
    private LocalTime sta;
    private String flightType;
    private Integer delay;
    private String status;
} 