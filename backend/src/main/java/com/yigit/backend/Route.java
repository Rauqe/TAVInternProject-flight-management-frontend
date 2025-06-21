package com.yigit.backend;

import jakarta.persistence.*;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_code", nullable = false)
    private Station origin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_code", nullable = false)
    private Station destination;

    // Constructors
    public Route() {}
    public Route(Station origin, Station destination) {
        this.origin = origin;
        this.destination = destination;
    }
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Station getOrigin() { return origin; }
    public void setOrigin(Station origin) { this.origin = origin; }
    public Station getDestination() { return destination; }
    public void setDestination(Station destination) { this.destination = destination; }
} 