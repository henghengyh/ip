package agnes.parser;

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
    FIND,
    UNKNOWN;

    public static Command from(String input) {
        try {
            return Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}