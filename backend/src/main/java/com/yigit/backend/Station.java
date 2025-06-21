package com.yigit.backend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Station {

    @Id
    @Column(length = 4)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    // Constructors
    public Station() {
    }

    public Station(String code, String name, String country) {
        this.code = code;
        this.name = name;
        this.country = country;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
} 