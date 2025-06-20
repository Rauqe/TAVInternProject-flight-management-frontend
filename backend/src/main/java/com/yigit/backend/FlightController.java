package com.yigit.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:5173")
public class FlightController {
    @Autowired
    private FlightRepository repo;

    @GetMapping
    public List<Flight> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Flight create(@RequestBody Flight flight) {
        return repo.save(flight);
    }
} 