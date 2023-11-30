package com.siseth.weatherStation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO {
    private Long size;
    private Long totalElements;
    private Long totalPages;
    private Long number;
}