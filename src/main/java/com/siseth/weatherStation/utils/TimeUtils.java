package com.siseth.weatherStation.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final DateTimeFormatter offsetDateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static LocalDateTime convertToLocaDateTime(OffsetDateTime offsetDateTime) {
        long epochSecond = offsetDateTime.toEpochSecond();
        return LocalDateTime.ofEpochSecond(epochSecond, 0, offsetDateTime.getOffset());
    }

    public static OffsetDateTime getDateInOffDateFormat(LocalDateTime localDateTime) {
        return localDateTime.atOffset(getLocalDateTimeZoneOffset(localDateTime));
    }
    public static OffsetDateTime translateDateFromString(String dateString) {
        return OffsetDateTime.parse(dateString, offsetDateFormatter);
    }

    public static ZoneOffset getLocalDateTimeZoneOffset(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneOffset.systemDefault()).getOffset();
    }

    public static String getDateInStringFormat(LocalDateTime date) {
        return date.format(dateTimeFormatter);
    }

    public static String getOffDateInStringFormat(OffsetDateTime date) {
        return date.format(dateTimeFormatter);
    }
}
