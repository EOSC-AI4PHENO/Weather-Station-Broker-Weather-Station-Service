package com.siseth.weatherStation.dto.weatherStation;

import com.siseth.weatherStation.component.entity.WeatherStation;
import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.observationStation.ObservationStationDTO;
import com.siseth.weatherStation.enumerate.StationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherStationDTO {
    private Long stationId;
    private String observationStationId;
    private String name;
    private String tercCode;
    private String wktLocation;
    private Double latitude;
    private Double longitude;
    private String ownerId;
    private StationType stationType;
    private Boolean active;
    private List<LinksDTO> links;

    public WeatherStationDTO(WeatherStation weatherStation) {
        this.stationId = weatherStation.getId();
        this.observationStationId = weatherStation.getObservationStationId();
        this.name = weatherStation.getName();
        this.latitude = weatherStation.getLatitude();
        this.longitude = weatherStation.getLongitude();
        this.stationType = weatherStation.getType();
        this.active = weatherStation.isActive();
        this.links = weatherStation.getLinks().stream().map(LinksDTO::new).collect(Collectors.toList());
    }

    public WeatherStationDTO(ObservationStationDTO c) {
        this.stationId = null;
        this.observationStationId = c.getId();
        this.name = c.getName();
        this.wktLocation = c.getWktLocation();
        this.tercCode = c.getTercCode();
        this.latitude = c.getLatitude();
        this.longitude = c.getLongitude();
        this.stationType = c.getStationType();
        this.ownerId = c.getOwnerId();
        this.active = true;
        this.links = c.getLinks();
    }
}