package com.yigit.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigit.backend.service.FlightKafkaProducer;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:5173")
public class FlightController {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private AircraftTypeRepository aircraftTypeRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FlightKafkaProducer flightKafkaProducer;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public List<Flight> getAll() {
        return flightRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Flight> create(@RequestBody FlightRequestDTO flightDTO) {
        try {
            Airline airline = airlineRepository.findById(flightDTO.getAirlineCode())
                    .orElseThrow(() -> new NoSuchElementException("Airline not found with code: " + flightDTO.getAirlineCode()));
            AircraftType aircraftType = aircraftTypeRepository.findById(flightDTO.getAircraftTypeCode())
                    .orElseThrow(() -> new NoSuchElementException("AircraftType not found with code: " + flightDTO.getAircraftTypeCode()));
            Station origin = stationRepository.findById(flightDTO.getOriginCode())
                    .orElseThrow(() -> new NoSuchElementException("Station not found with code: " + flightDTO.getOriginCode()));
            Station destination = stationRepository.findById(flightDTO.getDestinationCode())
                    .orElseThrow(() -> new NoSuchElementException("Station not found with code: " + flightDTO.getDestinationCode()));

            Flight flight = new Flight();
            flight.setFlightNumber(flightDTO.getFlightNumber());
            flight.setAirline(airline);
            flight.setAircraftType(aircraftType);
            flight.setOrigin(origin);
            flight.setDestination(destination);
            flight.setFlightDate(flightDTO.getFlightDate());
            flight.setStd(flightDTO.getStd());
            flight.setSta(flightDTO.getSta());
            flight.setFlightType(flightDTO.getFlightType());
            flight.setDelay(flightDTO.getDelay());
            flight.setStatus(flightDTO.getStatus());

            Flight savedFlight = flightRepository.save(flight);
            try {
                String flightsJson = objectMapper.writeValueAsString(flightRepository.findAll());
                flightKafkaProducer.sendFlightUpdate(flightsJson);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return ResponseEntity.ok(savedFlight);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> update(@PathVariable Long id, @RequestBody FlightRequestDTO flightDTO) {
        try {
            Flight existingFlight = flightRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Flight not found with id: " + id));

            Airline airline = airlineRepository.findById(flightDTO.getAirlineCode())
                    .orElseThrow(() -> new NoSuchElementException("Airline not found with code: " + flightDTO.getAirlineCode()));
            AircraftType aircraftType = aircraftTypeRepository.findById(flightDTO.getAircraftTypeCode())
                    .orElseThrow(() -> new NoSuchElementException("AircraftType not found with code: " + flightDTO.getAircraftTypeCode()));
            Station origin = stationRepository.findById(flightDTO.getOriginCode())
                    .orElseThrow(() -> new NoSuchElementException("Station not found with code: " + flightDTO.getOriginCode()));
            Station destination = stationRepository.findById(flightDTO.getDestinationCode())
                    .orElseThrow(() -> new NoSuchElementException("Station not found with code: " + flightDTO.getDestinationCode()));

            existingFlight.setFlightNumber(flightDTO.getFlightNumber());
            existingFlight.setAirline(airline);
            existingFlight.setAircraftType(aircraftType);
            existingFlight.setOrigin(origin);
            existingFlight.setDestination(destination);
            existingFlight.setFlightDate(flightDTO.getFlightDate());
            existingFlight.setStd(flightDTO.getStd());
            existingFlight.setSta(flightDTO.getSta());
            existingFlight.setFlightType(flightDTO.getFlightType());
            existingFlight.setDelay(flightDTO.getDelay());
            existingFlight.setStatus(flightDTO.getStatus());

            Flight updatedFlight = flightRepository.save(existingFlight);
            try {
                String flightsJson = objectMapper.writeValueAsString(flightRepository.findAll());
                flightKafkaProducer.sendFlightUpdate(flightsJson);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return ResponseEntity.ok(updatedFlight);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!flightRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        flightRepository.deleteById(id);
        try {
            String flightsJson = objectMapper.writeValueAsString(flightRepository.findAll());
            flightKafkaProducer.sendFlightUpdate(flightsJson);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.noContent().build();
    }
} 