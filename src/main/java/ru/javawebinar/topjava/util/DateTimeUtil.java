package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        if (startTime == null && endTime == null) {
            return true;
        } else if (startTime != null && endTime == null) {
            return lt.compareTo(startTime) >= 0 && lt.compareTo(LocalTime.MAX) < 0;
        } else if (startTime == null) {
            return lt.compareTo(LocalTime.MIN) >= 0 && lt.compareTo(endTime) < 0;
        }
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static boolean isBetweenDates(LocalDate lt, LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return true;
        } else if (startDate != null && endDate == null) {
            return lt.compareTo(startDate) >= 0;
        } else if (startDate == null) {
            return lt.compareTo(endDate) <= 0;
        }
        return lt.compareTo(startDate) >= 0 && lt.compareTo(endDate) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}
