package com.yigit.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/flight-types")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasAuthority('ADMIN')")
public class FlightTypeController {
    @Autowired
    private FlightTypeRepository flightTypeRepository;

    @GetMapping
    public List<FlightType> getAllFlightTypes() {
        return flightTypeRepository.findAll();
    }

    @PostMapping
    public FlightType createFlightType(@RequestBody FlightType flightType) {
        return flightTypeRepository.save(flightType);
    }

    @DeleteMapping("/{id}")
    public void deleteFlightType(@PathVariable String id) {
        flightTypeRepository.deleteById(id);
    }
} 