package agnes.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles complex operations involving the reference to Date and/or Time
 * <p>
 * The {@code DateTimeUtil} contains static methods which acts as the bridge between
 * user's interface's input of date-time reference in String format and the program's
 * date-time reference in {@code LocalDate} and {@code LocalDateTime}.
 */
public class DateTimeUtil {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Convert's the user's {@code String} input of DateTime into {@code LocalDateTime} object.
     * <p>
     * The DateTimeParseException is meant to be caught and represented by a created Exception class.
     * </p>
     *
     * @param input                         The full user input string.
     * @throws DateTimeParseException       If the input format is incorrect.
     */
    public static LocalDateTime parseDateTime(String input) throws DateTimeParseException {
        try {
            return LocalDateTime.parse(input, DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            LocalDate date = LocalDate.parse(input, DATE_FORMAT);
            return date.atStartOfDay(); //we have to do this to fit the type check
        }
    }

    /**
     * Converts a {@code LocalDateTime} object into a {@code String} DateTime for display.
     *
     * @param dt    The {@code LocalDateTime} object
     */
    public static String formatDateTime(LocalDateTime dt) {
        DateTimeFormatter outDate = DateTimeFormatter.ofPattern("MMM dd yyyy");
        DateTimeFormatter outDateTime = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

        if (dt.toLocalTime().equals(java.time.LocalTime.MIDNIGHT)) {
            return dt.format(outDate);
        }
        return dt.format(outDateTime);
    }
}
