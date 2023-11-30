package com.siseth.weatherStation.dto.meteo;

import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.PageDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MeteoLocationResponseDTO {
    private List<MeteoLocationDataDTO> content;
    private List<LinksDTO> links;
    private PageDTO page;
    
}