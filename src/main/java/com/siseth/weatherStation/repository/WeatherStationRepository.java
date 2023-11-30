package com.siseth.weatherStation.repository;

import com.siseth.weatherStation.component.entity.WeatherStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WeatherStationRepository extends JpaRepository<WeatherStation, Long> {

    List<WeatherStation> findAllByIdIn(Set<Long> ids);

    boolean existsByObservationStationId(String observationStationId);

    Optional<WeatherStation> findByObservationStationId(String observationStationId);
}
