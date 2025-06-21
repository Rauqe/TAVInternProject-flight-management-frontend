package com.yigit.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reference/flight-types")
@CrossOrigin(origins = "http://localhost:5173")
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

    @DeleteMapping("/{code}")
    public void deleteFlightType(@PathVariable String code) {
        flightTypeRepository.deleteById(code);
    }
} 