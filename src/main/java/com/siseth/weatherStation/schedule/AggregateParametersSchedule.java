package com.siseth.weatherStation.schedule;

import com.siseth.weatherStation.component.entity.MeteoData;
import com.siseth.weatherStation.component.entity.WeatherStation;
import com.siseth.weatherStation.component.util.MeteoDataAggregateUtil;
import com.siseth.weatherStation.repository.MeteoDataRepository;
import com.siseth.weatherStation.repository.WeatherStationRepository;
import com.siseth.weatherStation.service.MeteoDataService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AggregateParametersSchedule {

    private final MeteoDataRepository meteoDataRepository;

    private final WeatherStationRepository weatherStationRepository;


    @SneakyThrows
    @Scheduled(initialDelay = 10000L, fixedRate = 60000000L)
    @Transactional
    public void synchronize() {

        log.debug("Start aggregate parameter");
        LocalDate dateReference = LocalDate.now().minusDays(2);

        List<WeatherStation> weatherStationList = weatherStationRepository.findAll();

        for (WeatherStation weatherStation : weatherStationList) {
            List<MeteoData> meteoData = meteoDataRepository.getDataToAggregate(weatherStation, dateReference);

            Map<LocalDate, List<MeteoData>> map =
                            meteoData
                                    .stream()
                                    .collect(Collectors.groupingBy(x -> x.getMeasurementDate().toLocalDate()));
            List<MeteoData> aggregated =
                    map.entrySet()
                            .stream()
                            .map(x ->
                                    new MeteoData(
                                            weatherStation,
                                            x.getKey(),
                                            MeteoDataAggregateUtil.average(
                                                    x.getValue()
                                                            .stream()
                                                            .map(MeteoData::getAirTemperature)
                                                            .filter(Objects::nonNull)
                                                            .collect(Collectors.toList())),
                                            MeteoDataAggregateUtil.average(
                                                    x.getValue()
                                                            .stream()
                                                            .map(MeteoData::getRelativeHumidity)
                                                            .filter(Objects::nonNull)
                                                            .collect(Collectors.toList())),

                                            MeteoDataAggregateUtil.sum(
                                                    x.getValue()
                                                            .stream()
                                                            .map(MeteoData::getPrecipitation)
                                                            .filter(Objects::nonNull)
                                                            .collect(Collectors.toList()))
                                                    )
                            )
                            .collect(Collectors.toList());

            meteoDataRepository.saveAll(aggregated);



        }


        log.debug("End aggregate parameter");

    }

}
