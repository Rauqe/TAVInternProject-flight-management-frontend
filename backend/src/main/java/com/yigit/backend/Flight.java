package com.yigit.backend;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String flightNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airline_code", nullable = false)
    private Airline airline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aircraft_type_code", nullable = false)
    private AircraftType aircraftType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_code", nullable = false)
    private Station origin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_code", nullable = false)
    private Station destination;

    @Column(nullable = false)
    private LocalDate flightDate;

    @Column(nullable = false)
    private LocalTime std; // Scheduled Time of Departure

    @Column(nullable = false)
    private LocalTime sta; // Scheduled Time of Arrival

    @Column(nullable = false)
    private String flightType; // e.g., Passenger, Cargo

    private Integer delay; // in minutes

    @Column(nullable = false)
    private String status; // e.g., Scheduled, Cancelled

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public AircraftType getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(AircraftType aircraftType) {
        this.aircraftType = aircraftType;
    }

    public Station getOrigin() {
        return origin;
    }

    public void setOrigin(Station origin) {
        this.origin = origin;
    }

    public Station getDestination() {
        return destination;
    }

    public void setDestination(Station destination) {
        this.destination = destination;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }

    public LocalTime getStd() {
        return std;
    }

    public void setStd(LocalTime std) {
        this.std = std;
    }

    public LocalTime getSta() {
        return sta;
    }

    public void setSta(LocalTime sta) {
        this.sta = sta;
    }

    public String getFlightType() {
        return flightType;
    }

    public void setFlightType(String flightType) {
        this.flightType = flightType;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 