package com.siseth.weatherStation.component.entity;

import com.siseth.weatherStation.utils.TimeUtils;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Component
public class TimeRange {

    private LocalDateTime from;
    private LocalDateTime to;

    public String getFromString() {
       return TimeUtils.getDateInStringFormat(from);
    }
    public String getToString() {
        return TimeUtils.getDateInStringFormat(to);
    }

    public OffsetDateTime getOffsetFrom() {
        return TimeUtils.getDateInOffDateFormat(from);
    }
    public OffsetDateTime getOffsetTo() {
        return TimeUtils.getDateInOffDateFormat(to);
    }

}
