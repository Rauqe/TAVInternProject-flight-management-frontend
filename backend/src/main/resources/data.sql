-- Reference Data Initialization

-- Airlines
INSERT INTO airline (code, name) VALUES 
('TK', 'Turkish Airlines'),
('LH', 'Lufthansa'),
('BA', 'British Airways'),
('AF', 'Air France'),
('KL', 'KLM Royal Dutch Airlines'),
('IB', 'Iberia'),
('AZ', 'Alitalia'),
('OS', 'Austrian Airlines'),
('LX', 'Swiss International Air Lines'),
('SK', 'SAS Scandinavian Airlines');

-- Stations (Airports)
INSERT INTO station (code, name, country) VALUES 
('IST', 'Istanbul Airport', 'Turkey'),
('ESB', 'Ankara Esenboğa Airport', 'Turkey'),
('ADB', 'İzmir Adnan Menderes Airport', 'Turkey'),
('AYT', 'Antalya Airport', 'Turkey'),
('FRA', 'Frankfurt Airport', 'Germany'),
('MUC', 'Munich Airport', 'Germany'),
('LHR', 'London Heathrow Airport', 'United Kingdom'),
('CDG', 'Charles de Gaulle Airport', 'France'),
('AMS', 'Amsterdam Airport Schiphol', 'Netherlands'),
('MAD', 'Madrid Barajas Airport', 'Spain'),
('FCO', 'Rome Fiumicino Airport', 'Italy'),
('VIE', 'Vienna International Airport', 'Austria'),
('ZRH', 'Zurich Airport', 'Switzerland'),
('CPH', 'Copenhagen Airport', 'Denmark'),
('JFK', 'John F. Kennedy International Airport', 'United States'),
('LAX', 'Los Angeles International Airport', 'United States'),
('DXB', 'Dubai International Airport', 'United Arab Emirates'),
('DOH', 'Hamad International Airport', 'Qatar'),
('NRT', 'Narita International Airport', 'Japan'),
('SIN', 'Singapore Changi Airport', 'Singapore');

-- Aircraft Types
INSERT INTO aircraft_type (code, name) VALUES 
('B738', 'Boeing 737-800'),
('B739', 'Boeing 737-900'),
('B77W', 'Boeing 777-300ER'),
('B788', 'Boeing 787-8 Dreamliner'),
('B789', 'Boeing 787-9 Dreamliner'),
('A320', 'Airbus A320'),
('A321', 'Airbus A321'),
('A330', 'Airbus A330'),
('A350', 'Airbus A350'),
('A380', 'Airbus A380');

-- Flight Types
INSERT INTO flight_type (code, name) VALUES 
('PAX', 'Passenger'),
('CRG', 'Cargo'),
('CHT', 'Charter'),
('VIP', 'VIP'),
('MED', 'Medical'),
('MIL', 'Military');
