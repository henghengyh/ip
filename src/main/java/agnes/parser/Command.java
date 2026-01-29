package agnes.parser;
/**
 * Represents all valid user commands that Agnes can understand.
 * Each constant corresponds to a keyword parsed from user input.
 */
public enum Command {
    HI,
    BYE,
    LIST,
    ON,
    TODO,
    DEADLINE,
    EVENT,
    MARK,
    UNMARK,
    DELETE,
    UNKNOWN;

    /**
     * Converts a user input string into the corresponding {@code Command}.
     * <p>
     * The comparison is case-insensitive. If the input does not match any
     * known command, {@link Command#UNKNOWN} is returned instead of throwing
     * an exception.
     *
     * @param input The raw command word entered by the user.
     * @return The matching {@code Command}, or {@code UNKNOWN} if no match is found.
     */
    public static Command from(String input) {
        try {
            return Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}