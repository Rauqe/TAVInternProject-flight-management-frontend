package com.yigit.backend.mapper;

import com.yigit.backend.Flight;
import com.yigit.backend.FlightRequestDTO;
import com.yigit.backend.dto.FlightResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "aircraftType", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    Flight toEntity(FlightRequestDTO dto);

    @Mapping(source = "airline.code", target = "airlineCode")
    @Mapping(source = "airline.name", target = "airlineName")
    @Mapping(source = "aircraftType.code", target = "aircraftTypeCode")
    @Mapping(source = "aircraftType.name", target = "aircraftTypeName")
    @Mapping(source = "origin.code", target = "originCode")
    @Mapping(source = "origin.name", target = "originName")
    @Mapping(source = "destination.code", target = "destinationCode")
    @Mapping(source = "destination.name", target = "destinationName")
    FlightResponseDTO toDto(Flight entity);

    List<FlightResponseDTO> toDtoList(List<Flight> entities);
} 