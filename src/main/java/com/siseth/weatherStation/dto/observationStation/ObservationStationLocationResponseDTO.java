package com.siseth.weatherStation.dto.observationStation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.PageDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObservationStationLocationResponseDTO {
    private List<ObservationStationDTO> content;
    private PageDTO page;
    private List<LinksDTO> links;
}