package com.siseth.weatherStation.schedule;

import com.siseth.weatherStation.component.entity.MeteoData;
import com.siseth.weatherStation.component.entity.TimeRange;
import com.siseth.weatherStation.component.entity.WeatherStation;
import com.siseth.weatherStation.repository.WeatherStationRepository;
import com.siseth.weatherStation.service.DataSourceService;
import com.siseth.weatherStation.service.MeteoDataService;
import com.siseth.weatherStation.service.WeatherStationService;
import com.siseth.weatherStation.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SynchronizeWeatherStationSchedule {

    private final MeteoDataService meteoDataService;

    private final WeatherStationService stationService;

    private final DataSourceService dataSourceService;

    private final WeatherStationRepository weatherStationRepository;


//    @Scheduled(cron = "0 1 1 * * ?")
    @SneakyThrows
    @Scheduled(initialDelay = 10000L, fixedRate = 60000000L)
    @Transactional
    public void synchronize() {
        System.out.println("Synchronize Weather Station Data - start");
        long startTime = System.currentTimeMillis();
        //getting all configure data sources with weather stations
        List<WeatherStation> weatherStationList = getAllWeatherStationsForDSes();
        //max date range
        LocalDateTime maxTo = LocalDateTime.now();
        LocalDateTime maxFrom = maxTo.minusDays(2);
        //getting meteo data
        List<MeteoData> meteoData = getMeteoDataForStations(weatherStationList, maxTo, maxFrom);
        //save meteo data
        meteoDataService.saveData(meteoData);
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        System.out.println("Synchronize Weather Station Data - finished in :" + timeTaken + "millis.");
    }

    private List<WeatherStation> getAllWeatherStationsForDSes() {
//        List<ImageSourceShortResDTO> dses = dataSourceService.getAllDSesWithWeatherStation();
//        Set<Long> stationIds = dses.stream().map(ImageSourceShortResDTO::getStationId).collect(Collectors.toSet());
//        return stationService.getAllWeatherStationsForGivenIds(stationIds);
        return weatherStationRepository.findAll();
    }

    private List<MeteoData> getMeteoDataForStations(List<WeatherStation> weatherStationList, LocalDateTime maxTo, LocalDateTime maxFrom) {
        List<MeteoData> meteoData = new ArrayList<>();
        for(WeatherStation station :weatherStationList) {
            //checking from when data sources have meteo data - changing range
            LocalDateTime lastDataDate = Optional.ofNullable(meteoDataService.getLastDataForStation(station))
                            .map(x -> TimeUtils.convertToLocaDateTime(x).plusSeconds(1L))
                                    .orElse(maxFrom);

            List<MeteoData> meteoDataForStation = meteoDataService.downloadMeteoDataByStationIdInTime(station,
                    new TimeRange(maxFrom.isAfter(lastDataDate) ? maxFrom : lastDataDate, maxTo));
            meteoData.addAll(meteoDataForStation);
        }
        return meteoData;
    }
}
