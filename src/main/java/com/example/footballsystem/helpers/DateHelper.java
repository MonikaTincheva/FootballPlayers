package com.example.footballsystem.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class DateHelper {
    private static final List<String> dateFormatter = Arrays.asList(
            "M/d/yyyy",
            "MM/dd/yyyy",
            "d/MM/yyyy",
            "yyyy-MM-dd",
            "dd-MM-yyyy",
            "MM-dd-yyyy"
    );

    public static LocalDate parseDate(String dateStr) {
        for (String format : dateFormatter) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {

            }
        }
        throw new IllegalArgumentException("Invalid date format: " + dateStr);
    }
}
