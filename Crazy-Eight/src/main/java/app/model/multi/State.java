package app.model.multi;

/**
 * Represents the various states that can be associated with log messages or system events.
 * The states may be used to categorize or differentiate different types of messages
 * or events in the application's context.
 *
 * - System: Represents a general system-level event or message.
 * - Log: Represents a standard log message.
 * - Error: Represents an error message or event.
 */
public enum State {
    System,
    Log,
    Error,
}
