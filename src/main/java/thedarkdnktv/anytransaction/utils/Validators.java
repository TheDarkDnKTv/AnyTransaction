package thedarkdnktv.anytransaction.utils;

import jakarta.validation.ValidationException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Validators {

    public static ZonedDateTime validateZonedDateTime(String date) {
        try {
            return ZonedDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception e) {
            throw new ValidationException("Date format is invalid", e);
        }
    }

    public static double validateDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new ValidationException("Not a float number", e);
        }
    }
}
