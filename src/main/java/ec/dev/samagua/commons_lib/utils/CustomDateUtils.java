package ec.dev.samagua.commons_lib.utils;

import ec.dev.samagua.commons_lib.models.DateRange;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class CustomDateUtils {
    private CustomDateUtils() {
        // Prevent instantiation
    }

    public static DateRange getDateRange(String string) {
        // Split the string by comma to separate the two dates
        String[] fechas = string.split(",");

        // Convert each date (as LocalDate) to LocalDateTime at the start of the day
        LocalDateTime startDateTime = LocalDate.parse(fechas[0]).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(fechas[1]).atTime(23, 59, 59);

        return DateRange.builder()
                .startDate(startDateTime)
                .endDate(endDateTime)
                .build();

    }
}
