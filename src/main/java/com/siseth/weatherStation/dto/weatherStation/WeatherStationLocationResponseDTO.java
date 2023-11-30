package com.siseth.weatherStation.dto.weatherStation;

import com.siseth.weatherStation.dto.observationStation.ObservationStationLocationResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class WeatherStationLocationResponseDTO {
    private List<WeatherStationDTO> observationStations;
    private int totalCount;

    public WeatherStationLocationResponseDTO(ObservationStationLocationResponseDTO observationStationLocationResponseDTO) {
        //TODO
        this.observationStations = observationStationLocationResponseDTO.getContent().stream().map(WeatherStationDTO::new).collect(Collectors.toList());
//        this.totalCount = observationStationLocationResponseDTO.getTotalCount();
    }
}