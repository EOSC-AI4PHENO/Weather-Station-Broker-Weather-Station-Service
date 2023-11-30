package com.siseth.weatherStation.repository;

import com.siseth.weatherStation.component.entity.MeteoData;
import com.siseth.weatherStation.component.entity.WeatherStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public interface MeteoDataRepository extends JpaRepository<MeteoData, Long> {

    List<MeteoData> findAllByStationId(Long stationId);

    Page<MeteoData> findAllByStationId(Long stationId, Pageable pageable);

    @Query("SELECT md " +
            "FROM MeteoData md " +
            "INNER JOIN WeatherStation s ON md.station = s " +
            "WHERE s.id = :stationId AND md.aggregate = :aggregate AND " +
            " CAST(md.measurementDate AS LocalDate) >=  :dateFrom AND " +
            " CAST(md.measurementDate AS LocalDate) <=  :dateTo " +
            "ORDER by md.measurementDate ")
    Page<MeteoData> getAllByStationIdAndAggregate(Long stationId, MeteoData.AggregateType aggregate,
                                                  LocalDate dateFrom, LocalDate dateTo,
                                                  Pageable pageable);

    List<MeteoData> findByStationAndMeasurementDateBetween(WeatherStation station, OffsetDateTime fromDate, OffsetDateTime toDate);

    boolean existsByStationAndMeasurementDateBetween(WeatherStation station, OffsetDateTime fromDate, OffsetDateTime toDate);

    boolean existsByStationAndMeasurementDate(WeatherStation station, OffsetDateTime fromDate);


    @Query("SELECT MAX(md.measurementDate) FROM MeteoData md WHERE md.station = :station")
    OffsetDateTime findLatestMeasurementDateByStation(WeatherStation station);


    @Query("SELECT md " +
                    "FROM MeteoData md " +
                    "LEFT JOIN MeteoData agr ON md.station = agr.station AND " +
                    "   CAST(md.measurementDate AS LocalDate) = CAST(agr.measurementDate AS LocalDate) AND " +
                    "   agr.aggregate = 'DAILY' " +
                    "WHERE md.station = :station AND agr IS NULL AND md.aggregate = 'NONE' " +
                    " AND CAST(md.measurementDate AS LocalDate) < :dateReference")
    List<MeteoData> getDataToAggregate(WeatherStation station, LocalDate dateReference);

}
