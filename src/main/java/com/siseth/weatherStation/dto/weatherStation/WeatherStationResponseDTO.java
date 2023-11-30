package com.siseth.weatherStation.dto.weatherStation;

import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.PageDTO;
import com.siseth.weatherStation.dto.observationStation.ObservationStationResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class WeatherStationResponseDTO {
    private List<WeatherStationDTO> content;
    private PageDTO page;
    private List<LinksDTO> links;

    public WeatherStationResponseDTO(ObservationStationResponseDTO observationStationResponseDTO) {
        this.content = observationStationResponseDTO.getContent().stream().map(WeatherStationDTO::new).collect(Collectors.toList());
        this.page = observationStationResponseDTO.getPage();
        this.links =  observationStationResponseDTO.getLinks();
    }
}