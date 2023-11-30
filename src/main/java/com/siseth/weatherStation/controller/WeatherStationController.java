package com.siseth.weatherStation.controller;

import com.siseth.weatherStation.dto.weatherStation.WeatherStationDTO;
import com.siseth.weatherStation.dto.weatherStation.WeatherStationLocationResponseDTO;
import com.siseth.weatherStation.dto.weatherStation.WeatherStationResponseDTO;
import com.siseth.weatherStation.enumerate.StationType;
import com.siseth.weatherStation.service.WeatherStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather-station/station/weatherStation")
@Tag(name = "Weather Station Controller", description = "Endpoints for getting Weather Station Data")
public class WeatherStationController {

    private final WeatherStationService stationService;

    @Autowired
    public WeatherStationController(WeatherStationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/synchronize")
    public ResponseEntity<Void> synchronize() {
        stationService.synchronize();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/byId")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get Weather Stations by  Id", description = "Get Weather Stations (stationId, name, type, active) for given Id")
    public ResponseEntity<WeatherStationDTO> getWeatherStations(@RequestParam(name = "stationId") Long stationId) {
        return ResponseEntity.ok(stationService.getWeatherStationDTO(stationId));
    }

    @GetMapping("/observationStation")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get Weather Stations", description = "Get list of  Weather Stations (stationId, name, type, active)")
    public ResponseEntity<WeatherStationResponseDTO> getWeatherStations(@RequestParam(required = false, defaultValue = "") String contains,
                                                                        @RequestParam(required = false) StationType type,
                                                                        @RequestParam(required = false, defaultValue = "true") Boolean active,
                                                                        @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                        @RequestParam(required = false, defaultValue = "100000") Integer size,
                                                                        @RequestParam(required = false, defaultValue = "asc") String sort) {
        return ResponseEntity.ok(stationService.getWeatherStations(contains, type, active, page, size, sort));
    }

    @GetMapping("/observationStation/location")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get Weather Station Location data by location", description = "Get list of Weather Stations by location(stationId, name, type, active)")
    public ResponseEntity<WeatherStationLocationResponseDTO> getWeatherStationsByLocation( @RequestParam Double latitude,
                                                                                          @RequestParam Double longitude,
                                                                                          @RequestParam(required = false) StationType type,
                                                                                          @RequestParam(required = false) Integer distance,
                                                                                          @RequestParam(required = false, defaultValue = "true") Boolean active,
                                                                                          @RequestParam(required = false) Integer page,
                                                                                          @RequestParam(required = false) Integer size,
                                                                                          @RequestParam(required = false, defaultValue = "asc") String sort) {
        return ResponseEntity.ok(stationService.getWeatherStationsByLocation(latitude, longitude, type, distance, active, page, size, sort));
    }

    @PostMapping()
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Save Weather Station", description = "Save Weather Station - request body WeatherStationDTO")
    public ResponseEntity<Void> saveWeatherStations(@RequestBody WeatherStationDTO weatherStationDTO) {
        stationService.setWeatherStation(weatherStationDTO, null);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/byObservationStationId")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Save Weather Station", description = "Save Weather Station - by observationStationId ")
    public ResponseEntity<Void> saveWeatherStations(@RequestParam String observationStationId) {
        stationService.fetchAndSaveWeatherStation(observationStationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteAlarm(@RequestBody WeatherStationDTO weatherStationDTO) {
        return ResponseEntity.ok(stationService.deleteWeatherStationById(weatherStationDTO.getStationId()));
    }
}
