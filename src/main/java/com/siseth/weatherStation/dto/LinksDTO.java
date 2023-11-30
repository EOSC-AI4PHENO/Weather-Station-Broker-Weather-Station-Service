package com.siseth.weatherStation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinksDTO {
    private String rel;
    private String href;

    public LinksDTO(String linkString) {
        this.href = linkString;
    }
}