package com.siseth.weatherStation.dto.meteo;

import com.siseth.weatherStation.component.entity.WeatherStation;
import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.PageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeteoDataResponseDTO {
    private List<MeteoDataDTO> meteoDataList;
    private List<LinksDTO> links;
    private PageDTO page;

    public MeteoDataResponseDTO(MeteoStationResponseDTO meteoDataForStation, WeatherStation station) {
        this.meteoDataList = meteoDataForStation.getMeteoDataList().stream().map(m -> new MeteoDataDTO(m, station.getId())).collect(Collectors.toList());
        this.links = meteoDataForStation.getLinks();
        this.page = meteoDataForStation.getPage();
    }

}