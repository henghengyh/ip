package agnes.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDateTime parseDateTime(String input) throws DateTimeParseException {
        try {
            return LocalDateTime.parse(input, DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            LocalDate date = LocalDate.parse(input, DATE_FORMAT);
            return date.atStartOfDay(); //we have to do this to fit the type check
        }
    }

    public static String formatDateTime(LocalDateTime dt) {
        DateTimeFormatter outDate = DateTimeFormatter.ofPattern("MMM dd yyyy");
        DateTimeFormatter outDateTime = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

        if (dt.toLocalTime().equals(java.time.LocalTime.MIDNIGHT)) {
            return dt.format(outDate);
        }
        return dt.format(outDateTime);
    }
}
