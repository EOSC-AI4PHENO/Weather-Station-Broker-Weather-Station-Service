package com.siseth.weatherStation.controller;

import com.siseth.weatherStation.component.entity.MeteoData;
import com.siseth.weatherStation.dto.meteo.MeteoDataDTO;
import com.siseth.weatherStation.dto.meteo.MeteoDataResponseDTO;
import com.siseth.weatherStation.service.MeteoDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/weather-station/station/meteoData")
@Tag(name = "Meteo Data Controller", description = "Endpoints for getting Meteo Data for Weather Station")
public class MeteoDataController {

    private final MeteoDataService meteoDataService;

    @Autowired
    public MeteoDataController(MeteoDataService meteoDataService) {
        this.meteoDataService = meteoDataService;
    }

    @GetMapping("/byId")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get Meteo Data by  Id", description = "Get Meteo Data () for given Id")
    public ResponseEntity<MeteoDataDTO> getMeteoDataById(@RequestParam(name = "meteoDataId") Long meteoDataId) {
        return ResponseEntity.ok(meteoDataService.getMeteoDataByIdDTO(meteoDataId));
    }

    @GetMapping("/byStationId")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get Meteo Data for Station Id", description = "Get Meteo Data () for given station Id")
    public ResponseEntity<List<MeteoDataDTO>> getAllMeteoDataByStationId(@RequestParam(name = "stationId") Long stationId) {
        return ResponseEntity.ok(meteoDataService.getMeteoDataByStationIdDTO(stationId));
    }

    @GetMapping("/forceDownload/byStationId")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get Meteo Data for Station Id", description = "Get Meteo Data () for given station Id")
    public ResponseEntity<MeteoDataResponseDTO> forceDownloadMeteoDataForStationId(@RequestParam(required = false, name = "startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                                                   @RequestParam(required = false, name = "endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                                                                   @RequestParam(name = "stationId") Long stationId,
                                                                                   @RequestParam(name = "aggregate", defaultValue = "NONE") MeteoData.AggregateType aggregate,
                                                                                   @RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                                                   @RequestParam(name = "size", defaultValue = "1000") int pageSize,
                                                                                   @RequestParam(required = false, defaultValue = "asc") String sort) {
        return ResponseEntity.ok(meteoDataService.getMeteoDataByStationIdInTimeDTO(stationId, aggregate, startTime, endTime, pageNumber, pageSize, sort));
    }
}
