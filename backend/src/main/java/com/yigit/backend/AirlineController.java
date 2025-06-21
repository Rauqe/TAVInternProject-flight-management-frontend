package com.yigit.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reference/airlines")
@CrossOrigin(origins = "http://localhost:5173")
public class AirlineController {

    @Autowired
    private AirlineRepository airlineRepository;

    @GetMapping
    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    @PostMapping
    public Airline createAirline(@RequestBody Airline airline) {
        return airlineRepository.save(airline);
    }

    @DeleteMapping("/{id}")
    public void deleteAirline(@PathVariable String id) {
        airlineRepository.deleteById(id);
    }
} 