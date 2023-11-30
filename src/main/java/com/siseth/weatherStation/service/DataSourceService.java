package com.siseth.weatherStation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.weatherStation.constants.Constants;
import com.siseth.weatherStation.dto.imageSource.ImageSourceShortResDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
public class DataSourceService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<ImageSourceShortResDTO> getAllDSesWithWeatherStation() {
        String url = UriComponentsBuilder.fromHttpUrl(Constants.DATA_SOURCE_ADAPTER_BASE_URL)
                .path("/getAllSources/withStation")
                .toUriString();

        Object response = restTemplate.getForObject(url, ImageSourceShortResDTO.class);
        return response != null ?
                new ObjectMapper().convertValue(
                        response,
                        new TypeReference<>() {
                        }
                ) :
                Collections.emptyList();
    }
}