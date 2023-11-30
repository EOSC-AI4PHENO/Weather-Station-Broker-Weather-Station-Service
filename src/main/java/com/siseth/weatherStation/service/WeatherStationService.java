package com.siseth.weatherStation.service;

import com.siseth.weatherStation.component.entity.WeatherStation;
import com.siseth.weatherStation.constants.TranslationConstants;
import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.observationStation.ObservationStationDTO;
import com.siseth.weatherStation.dto.observationStation.ObservationStationLocationResponseDTO;
import com.siseth.weatherStation.dto.observationStation.ObservationStationResponseDTO;
import com.siseth.weatherStation.dto.weatherStation.WeatherStationDTO;
import com.siseth.weatherStation.dto.weatherStation.WeatherStationLocationResponseDTO;
import com.siseth.weatherStation.dto.weatherStation.WeatherStationResponseDTO;
import com.siseth.weatherStation.enumerate.StationType;
import com.siseth.weatherStation.feign.EdwinWeatherStationService;
import com.siseth.weatherStation.repository.WeatherStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WeatherStationService {

    private final EdwinWeatherStationService edwinWeatherStationService;
    private final WeatherStationRepository weatherStationRepository;
    @Autowired
    public WeatherStationService(EdwinWeatherStationService edwinWeatherStationService, WeatherStationRepository weatherStationRepository) {
        this.edwinWeatherStationService = edwinWeatherStationService;
        this.weatherStationRepository = weatherStationRepository;
    }

    public void synchronize() {
        WeatherStationResponseDTO dto = getWeatherStations(null, null, true, 0, 100000, null);
        dto.getContent().forEach(x -> setWeatherStation(x, null) );
    }

    public WeatherStationDTO saveOrUpdateEdwin(String edwinId) {

        WeatherStation weatherStation = weatherStationRepository.findByObservationStationId(edwinId)
                                                                            .orElse(new WeatherStation());

        if(weatherStation.getId() == null) {
            ObservationStationDTO dto = edwinWeatherStationService.getObservationStationById(edwinId);
            weatherStation.setObservationStationId(dto.getId());
            weatherStation.setName(dto.getName() != null ?
                                    dto.getName() :
                                    dto.getId());
            weatherStation.setType(dto.getStationType());
            weatherStation.setActive(true);
            weatherStation.setLongitude(dto.getLongitude());
            weatherStation.setLatitude(dto.getLatitude());
            weatherStation.setLinks(createLinks(dto.getLinks()));
            weatherStation = weatherStationRepository.save(weatherStation);
        }
        return new WeatherStationDTO(weatherStation);
    }

    public WeatherStationDTO findById(Long id) {
        return weatherStationRepository.findById(id)
                                                .map(WeatherStationDTO::new)
                                                .orElse(null);
    }

    public WeatherStationDTO getWeatherStationDTO(Long stationId) {
        return new WeatherStationDTO(getWeatherStation(stationId));
    }
    public WeatherStation getWeatherStation(Long stationId) {
        return weatherStationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException(TranslationConstants.WEATHER_STATION_NOT_EXISTS.toString()));
    }
    public List<WeatherStation> getAllWeatherStationsForGivenIds(Set<Long> stationIds) {
        return weatherStationRepository.findAllByIdIn(stationIds);
    }

    public WeatherStationResponseDTO getWeatherStations(String contains, StationType type, Boolean active, Integer page, Integer size, String sort) {
        ObservationStationResponseDTO observationStationResponseDTO = edwinWeatherStationService.getObservationStations(contains, type, active, page, size, sort);
        return new WeatherStationResponseDTO(observationStationResponseDTO);
    }

    public WeatherStationLocationResponseDTO getWeatherStationsByLocation(Double latitude, Double longitude, StationType type, Integer distance, Boolean active, Integer page, Integer size, String sort) {
        ObservationStationLocationResponseDTO observationStationLocationResponseDTO = edwinWeatherStationService.getObservationStationsByLocation(latitude, longitude,  distance, type, active, page, size, sort);
        return new WeatherStationLocationResponseDTO(observationStationLocationResponseDTO);
    }

    public void setWeatherStation(WeatherStationDTO weatherStationDTO, Long userId) {
    if(weatherStationDTO.getStationId() == null)
        addWeatherStation(weatherStationDTO);
    else
        editWeatherStation(weatherStationDTO);
    }

    private void editWeatherStation(WeatherStationDTO weatherStationDTO) {
        WeatherStation weatherStation = getWeatherStation(weatherStationDTO.getStationId());
        weatherStation.update(weatherStationDTO);
        weatherStationRepository.save(weatherStation);
    }

    private void addWeatherStation(WeatherStationDTO weatherStationDTO) {
        WeatherStation weatherStation = new WeatherStation();
        weatherStation.setObservationStationId(weatherStationDTO.getObservationStationId());
        weatherStation.setType(weatherStationDTO.getStationType());
        weatherStation.setActive(weatherStationDTO.getActive());
        weatherStation.setLongitude(weatherStationDTO.getLongitude());
        weatherStation.setLatitude(weatherStationDTO.getLatitude());
        weatherStation.setLinks(createLinks(weatherStationDTO.getLinks()));
        weatherStationRepository.save(weatherStation);
    }

    private void addWeatherStation(ObservationStationDTO observationStationDTO) {
        WeatherStation weatherStation = new WeatherStation();
        weatherStation.setObservationStationId(observationStationDTO.getId());
        weatherStation.setType(observationStationDTO.getStationType());
        weatherStation.setActive(true);
        weatherStation.setLongitude(observationStationDTO.getLongitude());
        weatherStation.setLatitude(observationStationDTO.getLatitude());
        weatherStation.setLinks(createLinks(observationStationDTO.getLinks()));
        weatherStationRepository.save(weatherStation);
    }

    private List<String> createLinks(List<LinksDTO> links) {
        return links.stream().map(LinksDTO::getHref).collect(Collectors.toList());
    }

    public String deleteWeatherStationById(Long weatherStationId) {
        WeatherStation weatherStation = getWeatherStation(weatherStationId);
        weatherStation.setActive(false);
        weatherStationRepository.save(weatherStation);
        return TranslationConstants.WEATHER_STATION_WAS_DELETED.toString();
    }


    public void fetchAndSaveWeatherStation(String observationStationId) {
       if(weatherStationRepository.existsByObservationStationId(observationStationId))
               throw new RuntimeException(TranslationConstants.WEATHER_STATION_EXISTING.toString());
        ObservationStationDTO observationStationDTO = edwinWeatherStationService.getObservationStationById(observationStationId);
        addWeatherStation(observationStationDTO);
    }
}