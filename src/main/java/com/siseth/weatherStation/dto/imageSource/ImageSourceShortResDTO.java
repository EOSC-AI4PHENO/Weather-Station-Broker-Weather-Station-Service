package com.siseth.weatherStation.dto.imageSource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSourceShortResDTO {

    private String name;
    private String desc;
    private String ip;
    private Integer port;
    private String cameraUser;
    private String cameraPassword;
    private Long stationId;

}
