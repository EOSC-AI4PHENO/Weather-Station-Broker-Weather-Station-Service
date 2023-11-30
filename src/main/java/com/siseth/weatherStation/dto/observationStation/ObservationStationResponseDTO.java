package com.siseth.weatherStation.dto.observationStation;

import com.siseth.weatherStation.dto.LinksDTO;
import com.siseth.weatherStation.dto.PageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ObservationStationResponseDTO {
    private List<ObservationStationDTO> content;
    private PageDTO page;
    private List<LinksDTO> links;
}