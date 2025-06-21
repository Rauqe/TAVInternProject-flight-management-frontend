package com.yigit.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reference/stations")
@CrossOrigin(origins = "http://localhost:5173")
public class StationController {

    @Autowired
    private StationRepository stationRepository;

    @GetMapping
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    @PostMapping
    public Station createStation(@RequestBody Station station) {
        return stationRepository.save(station);
    }

    @DeleteMapping("/{id}")
    public void deleteStation(@PathVariable String id) {
        stationRepository.deleteById(id);
    }
} 