package com.siseth.weatherStation.component.entity;

import com.siseth.weatherStation.component.entity.base.BaseEntity;
import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.meteo.MeteoStationDataDTO;
import com.siseth.weatherStation.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = "stations", name = "`MeteoData`")
@BatchSize(size = 1000)
public class MeteoData extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`stationId`", nullable = false)
    private WeatherStation station;

    @Column(name="`measurementDate`")
    private OffsetDateTime measurementDate;
    
    @Column(name="`airTemperature`")
    private Double airTemperature;
    
    @Column(name="`relativeHumidity`")
    private Double relativeHumidity;
    
    @Column(name="`insolation`")
    private Double insolation;
    
    @Column(name="`windSpeed`")
    private Double windSpeed;
    
    @Column(name="`windDirection`")
    private Double windDirection;
    
    @Column(name="`airPressure`")
    private Double airPressure;
    
    @Column(name="`precipitation`")
    private Double precipitation;
    
    @Column(name="`dewPointTemperature`")
    private Double dewPointTemperature;

    @Column(name="`aggregate`")
    @Enumerated(EnumType.STRING)
    private AggregateType aggregate;


    public enum AggregateType {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY,
        NONE
    }
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            schema = "stations",
            name="`MeteoLinks`",
            joinColumns=@JoinColumn(name="`meteo_data_id`")
    )
    @Column(name="`link`")
    private List<String> links = new ArrayList<>();

    public MeteoData(MeteoStationDataDTO meteoStationDataDTO, WeatherStation station) {
        this.station = station;
        this.measurementDate = TimeUtils.translateDateFromString(meteoStationDataDTO.getMeasurementDate());
        this.airTemperature = meteoStationDataDTO.getAirTemperature();
        this.relativeHumidity = meteoStationDataDTO.getRelativeHumidity();
        this.windSpeed = meteoStationDataDTO.getWindSpeed();
        this.windDirection = meteoStationDataDTO.getWindDirection();
        this.precipitation = meteoStationDataDTO.getPrecipitation();
        this.links = meteoStationDataDTO.getLinks().stream().map(LinksDTO::getHref).collect(Collectors.toList());
        this.aggregate = AggregateType.NONE;
    }

    public MeteoData(WeatherStation station, LocalDate date,
                     Double airTemperature, Double humidity,
                     Double precipitation) {
        this.station = station;
        this.measurementDate = date.atTime(0,0).atOffset(UTC);
        this.airTemperature = airTemperature;
        this.relativeHumidity = humidity;
        this.precipitation = precipitation;
        this.aggregate = AggregateType.DAILY;
    }
}
