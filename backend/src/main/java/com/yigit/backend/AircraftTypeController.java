package com.yigit.backend;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft-types")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasAuthority('ADMIN')")
public class AircraftTypeController {

    private final AircraftTypeRepository aircraftTypeRepository;

    public AircraftTypeController(AircraftTypeRepository aircraftTypeRepository) {
        this.aircraftTypeRepository = aircraftTypeRepository;
    }

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