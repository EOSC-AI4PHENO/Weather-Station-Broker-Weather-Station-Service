package com.siseth.weatherStation.service;

import com.siseth.weatherStation.component.entity.MeteoData;
import com.siseth.weatherStation.component.entity.TimeRange;
import com.siseth.weatherStation.component.entity.WeatherStation;
import com.siseth.weatherStation.constants.TranslationConstants;
import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.PageDTO;
import com.siseth.weatherStation.dto.meteo.MeteoDataDTO;
import com.siseth.weatherStation.dto.meteo.MeteoDataResponseDTO;
import com.siseth.weatherStation.dto.meteo.MeteoStationDataDTO;
import com.siseth.weatherStation.dto.meteo.MeteoStationResponseDTO;
import com.siseth.weatherStation.feign.EdwinWeatherStationService;
import com.siseth.weatherStation.repository.MeteoDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeteoDataService {

    private final EdwinWeatherStationService edwinWeatherStationService;

    private final WeatherStationService stationService;
    private final MeteoDataRepository meteoDataRepository;
    @Autowired
    public MeteoDataService(EdwinWeatherStationService edwinWeatherStationService, WeatherStationService stationService, MeteoDataRepository meteoDataRepository) {
        this.edwinWeatherStationService = edwinWeatherStationService;
        this.stationService = stationService;
        this.meteoDataRepository = meteoDataRepository;
    }

    private MeteoData getMeteoDataById(Long meteoDataId) {
        return meteoDataRepository.findById(meteoDataId)
                .orElseThrow(() -> new RuntimeException(TranslationConstants.METEO_DATA_NOT_EXISTS.toString()));
    }

    public MeteoDataDTO getMeteoDataByIdDTO(Long meteoDataId) {
        return new MeteoDataDTO(getMeteoDataById(meteoDataId));
    }

    public List<MeteoDataDTO> getMeteoDataByStationIdDTO(Long dataSourceId) {
        return translateToListMeteoDataDTO(getMeteoDataByStationId(dataSourceId));
    }

    private List<MeteoDataDTO> translateToListMeteoDataDTO(List<MeteoData> meteoDataByDataSourceId) {
        if(meteoDataByDataSourceId == null || meteoDataByDataSourceId.isEmpty())
            return new ArrayList<>();
        return meteoDataByDataSourceId.stream().map(MeteoDataDTO::new).collect(Collectors.toList());
    }

    private List<MeteoData> getMeteoDataByStationId(Long stationId) {
        return meteoDataRepository.findAllByStationId(stationId);
    }
    public MeteoDataResponseDTO getMeteoDataByStationIdInTimeDTO(Long stationId, MeteoData.AggregateType aggregate, LocalDateTime startTime, LocalDateTime endTime, int pageNumber, int pageSize, String sort) {
        WeatherStation station = stationService.getWeatherStation(stationId);
        TimeRange timeRange = getAdjustedTimeRange(startTime, endTime);
        if(!checkIfDataExistForWeatherStation(station, timeRange))
            return downloadMeteoDataByStationIdInTimeDTO(station, timeRange, pageNumber, pageSize, sort);
        return getMeteoDataByStationIdInPages(stationId, aggregate, timeRange, pageNumber, pageSize, sort);
    }

    private MeteoDataResponseDTO getMeteoDataByStationIdInPages(Long stationId, MeteoData.AggregateType aggregate,
                                                                TimeRange timeRange,
                                                                int pageNumber, int pageSize, String sortString) {
        Sort sort = getSortForMeteoData(sortString);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<MeteoData> meteoDataPage=
                meteoDataRepository.getAllByStationIdAndAggregate(stationId, aggregate,
                                                                    timeRange.getFrom().toLocalDate(),
                                                                    timeRange.getTo().toLocalDate(),
                                                                    pageable);

        List<MeteoDataDTO> meteoDataDTOList = meteoDataPage
                .stream()
                .map(MeteoDataDTO::new)
                .collect(Collectors.toList());

        List<LinksDTO> linksDTOList = meteoDataPage
                .stream()
                .flatMap(m -> m.getLinks().stream())
                .map(LinksDTO::new)
                .collect(Collectors.toList());

        PageDTO pageDTO = new PageDTO(
                (long) meteoDataPage.getSize(),
                meteoDataPage.getTotalElements(),
                (long) meteoDataPage.getTotalPages(),
                (long) meteoDataPage.getNumber()
        );

        return new MeteoDataResponseDTO(meteoDataDTOList, linksDTOList, pageDTO);
    }

    private Sort getSortForMeteoData(String sortString) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortString != null && sortString.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
       return Sort.by(direction, "measurementDate");
    }

    public MeteoDataResponseDTO downloadMeteoDataByStationIdInTimeDTO(WeatherStation station, TimeRange timeRange, Integer page, Integer size, String sort) {
        MeteoStationResponseDTO meteoDataForStation = edwinWeatherStationService.getMeteoDataForStation(station.getObservationStationId(), timeRange.getFromString(), timeRange.getToString(), page, size, sort);
        saveMeteoData(meteoDataForStation, station);
        return translateToListMeteoStationResponseDTO(meteoDataForStation, station);
    }
    public List<MeteoData> downloadMeteoDataByStationIdInTime(WeatherStation station, TimeRange timeRange) {
        List<MeteoStationDataDTO> meteoDataList = new ArrayList<>();
        MeteoStationResponseDTO meteoDataForStation = null;
        int page = 0;
        do {
            meteoDataForStation = edwinWeatherStationService.getMeteoDataForStation(station.getObservationStationId(), timeRange.getFromString(), timeRange.getToString(), page++, 1000, "asc");
            meteoDataList.addAll(meteoDataForStation.getMeteoDataList());
        } while((meteoDataForStation.getPage().getNumber() + 1) != meteoDataForStation.getPage().getTotalPages());
        return meteoDataList.stream().map(m -> new MeteoData(m, station))
//                .filter(x -> !meteoDataRepository.existsByStationAndMeasurementDate(station, x.getMeasurementDate()))
                .collect(Collectors.toList());
    }

    private void saveMeteoData(MeteoStationResponseDTO meteoDataForStation, WeatherStation station) {
        List<MeteoData> allMeteoData = meteoDataForStation.getMeteoDataList().stream().map(m -> new MeteoData(m, station)).collect(Collectors.toList());
        saveData(allMeteoData);
    }

    private boolean checkIfDataExistForWeatherStation(WeatherStation station, TimeRange timeRange) {
        return meteoDataRepository.existsByStationAndMeasurementDateBetween(station, timeRange.getOffsetFrom(), timeRange.getOffsetTo());
    }

    private MeteoDataResponseDTO translateToListMeteoStationResponseDTO(MeteoStationResponseDTO meteoDataForStation, WeatherStation station) {
        return new MeteoDataResponseDTO(meteoDataForStation, station);
    }

    private TimeRange getAdjustedTimeRange(LocalDateTime from, LocalDateTime to) {
        LocalDateTime adjustedTo = to != null ? to : LocalDateTime.now();
        LocalDateTime adjustedFrom = from != null && from.isAfter(adjustedTo.minusDays(90)) ? from : adjustedTo.minusDays(90);
        return new TimeRange(adjustedFrom, adjustedTo);
    }

    public OffsetDateTime getLastDataForStation(WeatherStation weatherStation) {
        return meteoDataRepository.findLatestMeasurementDateByStation(weatherStation);
    }

    public void saveData(List<MeteoData> meteoData) {
        meteoDataRepository.saveAll(meteoData);
    }
}