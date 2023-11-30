package com.siseth.weatherStation.feign;

import com.siseth.weatherStation.dto.imageSource.ImageSourceShortResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "source-adapter-service")
public interface SoftAdapterService {

    @GetMapping("/api/digital/source-adapter/getAllSources/withStation")
    List<ImageSourceShortResDTO> getAllDSesWithWeatherStation();
}
