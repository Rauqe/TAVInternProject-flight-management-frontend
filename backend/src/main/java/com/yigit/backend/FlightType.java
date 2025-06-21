package com.yigit.backend;

import jakarta.persistence.*;

@Entity
public class FlightType {
    @Id
    @Column(length = 16)
    private String code;

    @Column(nullable = false)
    private String name;

    // Constructors
    public FlightType() {}
    public FlightType(String code, String name) {
        this.code = code;
        this.name = name;
    }
    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
} 