package com.siseth.weatherStation.feign;

import com.siseth.weatherStation.dto.meteo.MeteoStationResponseDTO;
import com.siseth.weatherStation.dto.observationStation.ObservationStationDTO;
import com.siseth.weatherStation.dto.observationStation.ObservationStationLocationResponseDTO;
import com.siseth.weatherStation.dto.observationStation.ObservationStationResponseDTO;
import com.siseth.weatherStation.enumerate.StationType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "edwin-weather-station-service")
public interface EdwinWeatherStationService {

    @GetMapping("/api/weather-station/edwin/observationStation")
    public ObservationStationResponseDTO getObservationStations(@RequestParam(required = false) String contains,
                                                                @RequestParam(required = false) StationType type,
                                                                @RequestParam(required = false) Boolean active,
                                                                @RequestParam(required = false) Integer page,
                                                                @RequestParam(required = false) Integer size,
                                                                @RequestParam(required = false) String sort);

    @GetMapping("/api/weather-station/edwin/observationStation/{stationId}")
    public ObservationStationDTO getObservationStationById(@PathVariable("stationId") String stationId);

    @GetMapping("/api/weather-station/edwin/observationStation/location")
    public ObservationStationLocationResponseDTO getObservationStationsByLocation(@RequestParam("latitude") Double latitude,
                                                                                  @RequestParam("longitude") Double longitude,
                                                                                  @RequestParam(required = false) Integer distance,
                                                                                  @RequestParam(required = false) StationType type,
                                                                                  @RequestParam(required = false) Boolean active,
                                                                                  @RequestParam(required = false) Integer page,
                                                                                  @RequestParam(required = false) Integer size,
                                                                                  @RequestParam(required = false) String sort);

    @GetMapping("/api/weather-station/edwin/meteo/station")
    public MeteoStationResponseDTO getMeteoDataForStation(@RequestParam("stationId") String stationId,
                                                          @RequestParam(required = false) String after,
                                                          @RequestParam(required = false) String before,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) String sort);

    @GetMapping("/api/weather-station/edwin/meteo/station/{latitude}/{longitude}")
    public MeteoStationResponseDTO getMeteoDataByLocation(@PathVariable("latitude") Double latitude,
                                                          @PathVariable("longitude") Double longitude,
                                                          @RequestParam(required = false) Integer stationsCount,
                                                          @RequestParam(required = false) String after,
                                                          @RequestParam(required = false) String before,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) String sort);
}
