package com.yigit.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reference/aircraft-types")
@CrossOrigin(origins = "http://localhost:5173")
public class AircraftTypeController {

    @Autowired
    private AircraftTypeRepository aircraftTypeRepository;

    @GetMapping
    public List<AircraftType> getAllAircraftTypes() {
        return aircraftTypeRepository.findAll();
    }

    @PostMapping
    public AircraftType createAircraftType(@RequestBody AircraftType aircraftType) {
        return aircraftTypeRepository.save(aircraftType);
    }

    @DeleteMapping("/{id}")
    public void deleteAircraftType(@PathVariable String id) {
        aircraftTypeRepository.deleteById(id);
    }
} 