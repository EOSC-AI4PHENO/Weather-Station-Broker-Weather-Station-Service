package com.siseth.weatherStation.component.util;

import com.siseth.weatherStation.component.entity.MeteoData;

import java.util.List;
import java.util.Objects;

public class MeteoDataAggregateUtil {


    public static Double average(List<Double> doubleList) {
        Double size = Long.valueOf(doubleList.size()).doubleValue();
        return size == 0 ?
                0 :
                MeteoDataAggregateUtil.sum(doubleList) / size;
    }



    public static Double sum(List<Double> doubleList) {
        return doubleList.stream()
                .filter(Objects::nonNull)
                .reduce((double) 0, Double::sum);
    }


}
