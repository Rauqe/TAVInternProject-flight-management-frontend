package com.yigit.backend;

import java.time.LocalDate;
import java.time.LocalTime;

public class FlightRequestDTO {
    private String flightNumber;
    private String airlineCode;
    private String aircraftTypeCode;
    private String originCode;
    private String destinationCode;
    private LocalDate flightDate;
    private LocalTime std;
    private LocalTime sta;
    private String flightType;
    private Integer delay;
    private String status;

    // Getters and Setters
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public String getAirlineCode() { return airlineCode; }
    public void setAirlineCode(String airlineCode) { this.airlineCode = airlineCode; }
    public String getAircraftTypeCode() { return aircraftTypeCode; }
    public void setAircraftTypeCode(String aircraftTypeCode) { this.aircraftTypeCode = aircraftTypeCode; }
    public String getOriginCode() { return originCode; }
    public void setOriginCode(String originCode) { this.originCode = originCode; }
    public String getDestinationCode() { return destinationCode; }
    public void setDestinationCode(String destinationCode) { this.destinationCode = destinationCode; }
    public LocalDate getFlightDate() { return flightDate; }
    public void setFlightDate(LocalDate flightDate) { this.flightDate = flightDate; }
    public LocalTime getStd() { return std; }
    public void setStd(LocalTime std) { this.std = std; }
    public LocalTime getSta() { return sta; }
    public void setSta(LocalTime sta) { this.sta = sta; }
    public String getFlightType() { return flightType; }
    public void setFlightType(String flightType) { this.flightType = flightType; }
    public Integer getDelay() { return delay; }
    public void setDelay(Integer delay) { this.delay = delay; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 