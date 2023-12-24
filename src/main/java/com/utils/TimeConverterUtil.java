package com.utils;

import java.time.*;

public class TimeConverterUtil {

    private static final String DEFAULT_UTC_TIME_ZONE = "UTC";

    private TimeConverterUtil() {

    }

    public static LocalTime convertInstantToLocalTime(Instant time) {
        return LocalTime.ofInstant(time,ZoneId.of(DEFAULT_UTC_TIME_ZONE));
    }

    public static LocalDateTime convertInstantToLocalDateTime(Instant time) {
        return LocalDateTime.ofInstant(time, ZoneId.of(DEFAULT_UTC_TIME_ZONE));
    }

    public static LocalDate convertInstantToLocalDate(Instant time) {
        return LocalDate.ofInstant(time, ZoneId.of(DEFAULT_UTC_TIME_ZONE));
    }

    public static Instant convertLocalDateToInstant(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.of(DEFAULT_UTC_TIME_ZONE)).toInstant();
    }
}
