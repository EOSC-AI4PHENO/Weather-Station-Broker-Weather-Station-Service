package com.siseth.weatherStation.dto.meteo;

import com.siseth.weatherStation.component.entity.MeteoData;
import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.utils.TimeUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class MeteoDataDTO {
    private Long stationId;
    private String measurementDate;
    private Double airTemperature;
    private Double relativeHumidity;
    private Double insolation;
    private Double windSpeed;
    private Double windDirection;
    private Double airPressure;
    private Double precipitation;
    private Double dewPointTemperature;
    private List<LinksDTO> links;
    public MeteoDataDTO(MeteoData meteoData) {
        this.stationId = meteoData.getStation() != null ? meteoData.getStation().getId() : null;
        this.measurementDate = TimeUtils.getOffDateInStringFormat(meteoData.getMeasurementDate());
        this.airTemperature = meteoData.getAirTemperature();
        this.relativeHumidity = meteoData.getRelativeHumidity();
        this.insolation = meteoData.getInsolation();
        this.windSpeed = meteoData.getWindSpeed();
        this.windDirection = meteoData.getWindDirection();
        this.airPressure = meteoData.getAirPressure();
        this.precipitation = meteoData.getPrecipitation();
        this.dewPointTemperature = meteoData.getDewPointTemperature();
        this.links = meteoData.getLinks().stream().map(LinksDTO::new).collect(Collectors.toList());
    }

    public MeteoDataDTO(MeteoStationDataDTO meteoStationDataDTO, Long stationId) {
        this.stationId = stationId;
        this.measurementDate = meteoStationDataDTO.getMeasurementDate();
        this.airTemperature = meteoStationDataDTO.getAirTemperature();
        this.relativeHumidity = meteoStationDataDTO.getRelativeHumidity();
        this.windSpeed = meteoStationDataDTO.getWindSpeed();
        this.windDirection = meteoStationDataDTO.getWindDirection();
        this.precipitation = meteoStationDataDTO.getPrecipitation();
        this.links = meteoStationDataDTO.getLinks();
    }

}