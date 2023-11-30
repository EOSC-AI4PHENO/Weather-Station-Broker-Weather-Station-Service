package com.siseth.weatherStation.dto.observationStation;

import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.enumerate.StationType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ObservationStationDTO {
    private String id;
    private String name;
    private String tercCode;
    private String wktLocation;
    private Double latitude;
    private Double longitude;
    private String ownerId;
    private StationType stationType;
    private List<LinksDTO> links;
}