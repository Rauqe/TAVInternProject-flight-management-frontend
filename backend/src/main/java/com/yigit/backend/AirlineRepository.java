package com.yigit.backend;

import org.springframework.data.jpa.repository.JpaRepository;
 
public interface AirlineRepository extends JpaRepository<Airline, String> {
} 