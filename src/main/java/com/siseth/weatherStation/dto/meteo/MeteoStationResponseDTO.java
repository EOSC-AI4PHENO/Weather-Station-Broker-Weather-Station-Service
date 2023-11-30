package com.siseth.weatherStation.dto.meteo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.PageDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MeteoStationResponseDTO {
    @JsonProperty("content")
    private List<MeteoStationDataDTO> meteoDataList;
    private List<LinksDTO> links;
    private PageDTO page;
}