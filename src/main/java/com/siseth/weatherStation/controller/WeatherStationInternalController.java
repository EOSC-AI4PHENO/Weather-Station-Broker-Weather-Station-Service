package com.siseth.weatherStation.controller;

import com.siseth.weatherStation.dto.weatherStation.WeatherStationDTO;
import com.siseth.weatherStation.service.WeatherStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/weather-station/station/weatherStation")
@RequiredArgsConstructor
public class WeatherStationInternalController {

    private final WeatherStationService stationService;

    @PutMapping("/edwin")
    public ResponseEntity<WeatherStationDTO> saveWeatherStations(@RequestParam String edwinId) {
        return ResponseEntity.ok(stationService.saveOrUpdateEdwin(edwinId));
    }

    @GetMapping("/id")
    public ResponseEntity<WeatherStationDTO> getById(@RequestParam Long id) {
        return ResponseEntity.ok(stationService.findById(id));
    }

}
