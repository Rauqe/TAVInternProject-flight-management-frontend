package com.yigit.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private AircraftTypeRepository aircraftTypeRepository;

    @Autowired
    private FlightTypeRepository flightTypeRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no data exists
        if (airlineRepository.count() == 0) {
            initializeData();
        }
        
        // Initialize users if they don't exist
        if (userRepository.count() == 0) {
            initializeUsers();
        }
    }

    private void initializeUsers() {
        // Create admin user
        User adminUser = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .role(Role.ADMIN)
            .build();
        userRepository.save(adminUser);

        // Create regular user
        User regularUser = User.builder()
            .username("user")
            .password(passwordEncoder.encode("user123"))
            .role(Role.USER)
            .build();
        userRepository.save(regularUser);
    }

    private void initializeData() {
        // Initialize Airlines
        List<Airline> airlines = Arrays.asList(
            new Airline("TK", "Turkish Airlines"),
            new Airline("LH", "Lufthansa"),
            new Airline("BA", "British Airways"),
            new Airline("AF", "Air France"),
            new Airline("KL", "KLM Royal Dutch Airlines"),
            new Airline("IB", "Iberia"),
            new Airline("AZ", "Alitalia"),
            new Airline("OS", "Austrian Airlines"),
            new Airline("LX", "Swiss International Air Lines"),
            new Airline("SK", "SAS Scandinavian Airlines")
        );
        airlineRepository.saveAll(airlines);

        // Initialize Stations
        List<Station> stations = Arrays.asList(
            new Station("IST", "Istanbul Airport", "Turkey"),
            new Station("ESB", "Ankara Esenboğa Airport", "Turkey"),
            new Station("ADB", "İzmir Adnan Menderes Airport", "Turkey"),
            new Station("AYT", "Antalya Airport", "Turkey"),
            new Station("FRA", "Frankfurt Airport", "Germany"),
            new Station("MUC", "Munich Airport", "Germany"),
            new Station("LHR", "London Heathrow Airport", "United Kingdom"),
            new Station("CDG", "Charles de Gaulle Airport", "France"),
            new Station("AMS", "Amsterdam Airport Schiphol", "Netherlands"),
            new Station("MAD", "Madrid Barajas Airport", "Spain"),
            new Station("FCO", "Rome Fiumicino Airport", "Italy"),
            new Station("VIE", "Vienna International Airport", "Austria"),
            new Station("ZRH", "Zurich Airport", "Switzerland"),
            new Station("CPH", "Copenhagen Airport", "Denmark"),
            new Station("JFK", "John F. Kennedy International Airport", "United States"),
            new Station("LAX", "Los Angeles International Airport", "United States"),
            new Station("DXB", "Dubai International Airport", "United Arab Emirates"),
            new Station("DOH", "Hamad International Airport", "Qatar"),
            new Station("NRT", "Narita International Airport", "Japan"),
            new Station("SIN", "Singapore Changi Airport", "Singapore")
        );
        stationRepository.saveAll(stations);

        // Initialize Aircraft Types
        List<AircraftType> aircraftTypes = Arrays.asList(
            new AircraftType("B738", "Boeing 737-800"),
            new AircraftType("B739", "Boeing 737-900"),
            new AircraftType("B77W", "Boeing 777-300ER"),
            new AircraftType("B788", "Boeing 787-8 Dreamliner"),
            new AircraftType("B789", "Boeing 787-9 Dreamliner"),
            new AircraftType("A320", "Airbus A320"),
            new AircraftType("A321", "Airbus A321"),
            new AircraftType("A330", "Airbus A330"),
            new AircraftType("A350", "Airbus A350"),
            new AircraftType("A380", "Airbus A380")
        );
        aircraftTypeRepository.saveAll(aircraftTypes);

        // Initialize Flight Types
        List<FlightType> flightTypes = Arrays.asList(
            new FlightType("PAX", "Passenger"),
            new FlightType("CRG", "Cargo"),
            new FlightType("CHT", "Charter"),
            new FlightType("VIP", "VIP"),
            new FlightType("MED", "Medical"),
            new FlightType("MIL", "Military")
        );
        flightTypeRepository.saveAll(flightTypes);

        // Initialize Flights
        Random random = new Random();
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        int daysInYear = (int) (endDate.toEpochDay() - startDate.toEpochDay() + 1);
        List<Flight> flights = Arrays.asList(
            // Turkish Airlines flights
            createFlight("TK1", "TK", "B77W", "IST", "JFK", randomDate2025(random, startDate, daysInYear), "10:30", "14:45", "PAX", 0),
            createFlight("TK2", "TK", "B789", "JFK", "IST", randomDate2025(random, startDate, daysInYear), "16:00", "09:30", "PAX", 15),
            createFlight("TK3", "TK", "B738", "IST", "FRA", randomDate2025(random, startDate, daysInYear), "08:15", "11:30", "PAX", 0),
            createFlight("TK4", "TK", "B738", "FRA", "IST", randomDate2025(random, startDate, daysInYear), "13:45", "18:00", "PAX", 0),
            createFlight("TK5", "TK", "A321", "IST", "LHR", randomDate2025(random, startDate, daysInYear), "09:00", "12:15", "PAX", 30),

            // Lufthansa flights
            createFlight("LH100", "LH", "A350", "FRA", "JFK", randomDate2025(random, startDate, daysInYear), "11:00", "14:30", "PAX", 0),
            createFlight("LH101", "LH", "A350", "JFK", "FRA", randomDate2025(random, startDate, daysInYear), "17:30", "08:45", "PAX", 0),
            createFlight("LH102", "LH", "A320", "FRA", "LHR", randomDate2025(random, startDate, daysInYear), "07:30", "08:45", "PAX", 0),
            createFlight("LH103", "LH", "A320", "LHR", "FRA", randomDate2025(random, startDate, daysInYear), "10:15", "13:30", "PAX", 0),

            // British Airways flights
            createFlight("BA100", "BA", "B77W", "LHR", "JFK", randomDate2025(random, startDate, daysInYear), "09:30", "12:45", "PAX", 0),
            createFlight("BA101", "BA", "B77W", "JFK", "LHR", randomDate2025(random, startDate, daysInYear), "15:00", "04:15", "PAX", 0),
            createFlight("BA102", "BA", "A320", "LHR", "CDG", randomDate2025(random, startDate, daysInYear), "08:00", "11:15", "PAX", 0),

            // Air France flights
            createFlight("AF100", "AF", "A350", "CDG", "JFK", randomDate2025(random, startDate, daysInYear), "10:00", "13:15", "PAX", 0),
            createFlight("AF101", "AF", "A350", "JFK", "CDG", randomDate2025(random, startDate, daysInYear), "16:30", "05:45", "PAX", 0),
            createFlight("AF102", "AF", "A320", "CDG", "LHR", randomDate2025(random, startDate, daysInYear), "07:15", "08:30", "PAX", 0),

            // KLM flights
            createFlight("KL100", "KL", "B789", "AMS", "JFK", randomDate2025(random, startDate, daysInYear), "09:45", "13:00", "PAX", 0),
            createFlight("KL101", "KL", "B789", "JFK", "AMS", randomDate2025(random, startDate, daysInYear), "15:30", "04:45", "PAX", 0),

            // Iberia flights
            createFlight("IB100", "IB", "A330", "MAD", "JFK", randomDate2025(random, startDate, daysInYear), "11:30", "14:45", "PAX", 0),
            createFlight("IB101", "IB", "A330", "JFK", "MAD", randomDate2025(random, startDate, daysInYear), "17:00", "06:15", "PAX", 0),

            // Cargo flights
            createFlight("TK9001", "TK", "B77W", "IST", "DXB", randomDate2025(random, startDate, daysInYear), "02:00", "06:30", "CRG", 0),
            createFlight("LH9001", "LH", "B77W", "FRA", "DOH", randomDate2025(random, startDate, daysInYear), "01:30", "08:00", "CRG", 0)
        );

        flightRepository.saveAll(flights);
    }

    private Flight createFlight(String flightNumber, String airlineCode, String aircraftTypeCode, 
                               String originCode, String destinationCode, LocalDate flightDate, 
                               String std, String sta, String flightType, Integer delay) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setAirline(airlineRepository.findById(airlineCode).orElse(null));
        flight.setAircraftType(aircraftTypeRepository.findById(aircraftTypeCode).orElse(null));
        flight.setOrigin(stationRepository.findById(originCode).orElse(null));
        flight.setDestination(stationRepository.findById(destinationCode).orElse(null));
        flight.setFlightDate(flightDate);
        flight.setStd(LocalTime.parse(std));
        flight.setSta(LocalTime.parse(sta));
        flight.setFlightType(flightType);
        flight.setDelay(delay);
        flight.setStatus("Scheduled");
        return flight;
    }

    private LocalDate randomDate2025(Random random, LocalDate startDate, int daysInYear) {
        return startDate.plusDays(random.nextInt(daysInYear));
    }
} 