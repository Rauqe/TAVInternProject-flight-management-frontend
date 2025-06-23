package com.yigit.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasAuthority('ADMIN')")
public class RouteController {
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private StationRepository stationRepository;

    @GetMapping
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    @PostMapping
    public Route createRoute(@RequestBody RouteDTO dto) {
        Route route = new Route();
        route.setOrigin(stationRepository.findById(dto.getOriginCode()).orElseThrow());
        route.setDestination(stationRepository.findById(dto.getDestinationCode()).orElseThrow());
        return routeRepository.save(route);
    }

    @DeleteMapping("/{id}")
    public void deleteRoute(@PathVariable Long id) {
        routeRepository.deleteById(id);
    }

    // DTO for route creation
    public static class RouteDTO {
        private String originCode;
        private String destinationCode;
        public String getOriginCode() { return originCode; }
        public void setOriginCode(String originCode) { this.originCode = originCode; }
        public String getDestinationCode() { return destinationCode; }
        public void setDestinationCode(String destinationCode) { this.destinationCode = destinationCode; }
    }
} 