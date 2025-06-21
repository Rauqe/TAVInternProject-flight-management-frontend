package com.yigit.backend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AircraftType {

    @Id
    @Column(length = 4)
    private String code;

    @Column(nullable = false)
    private String name;

    // Constructors
    public AircraftType() {
    }

    public AircraftType(String code, String name) {
        this.code = code;
        this.name = name;
    }


    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
} 