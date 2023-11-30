package com.siseth.weatherStation.component.entity;

import com.siseth.weatherStation.component.entity.base.BaseEntity;
import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.weatherStation.WeatherStationDTO;
import com.siseth.weatherStation.enumerate.StationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "`WeatherStation`", schema = "stations")
@BatchSize(size = 1000)
public class WeatherStation extends BaseEntity {

    @Column(name="`observationStationId`")
    private String observationStationId;

    @Column(name="`name`")
    private String name;

    @Column(name="`latitude`")
    private Double latitude;
    
    @Column(name="`longitude`")
    private Double longitude;
    
    @Column(name="`active`")
    private boolean active;

    @Column(name="`type`")
    @Enumerated(EnumType.STRING)
    private StationType type;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "station")
    private List<MeteoData> meteoData = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable( schema = "stations", name = "`WeatherStationLinks`", joinColumns = @JoinColumn(name = "weather_station_id"))
    @Column(name="`link`")
    private List<String> links = new ArrayList<>();

    public void update(WeatherStationDTO weatherStationDTO) {
        this.observationStationId = weatherStationDTO.getObservationStationId();
        this.name = weatherStationDTO.getName();
        this.latitude = weatherStationDTO.getLatitude();
        this.longitude = weatherStationDTO.getLongitude();
        this.active = weatherStationDTO.getActive();
        this.type = weatherStationDTO.getStationType();
        this.links = createLinks(weatherStationDTO.getLinks());
    }

    private List<String> createLinks(List<LinksDTO> links) {
        return links.stream().map(LinksDTO::getHref).collect(Collectors.toList());
    }
}
