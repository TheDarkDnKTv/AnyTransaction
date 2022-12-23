package thedarkdnktv.anytransaction.utils;

import jakarta.validation.ValidationException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Validators {

    public static ZonedDateTime validateZonedDateTime(String date) {
        try {
            return ZonedDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException();
        }
    }
}
