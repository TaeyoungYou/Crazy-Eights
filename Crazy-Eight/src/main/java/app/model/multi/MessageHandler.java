package app.model.multi;

/**
 * Represents a handler for processing messages in a standardized way.
 * This interface is designed to be implemented by classes that handle
 * specific types of input messages and perform corresponding actions.
 */
public interface MessageHandler {
    /**
     * Handles the provided message by performing specific processing as defined by the implementing class.
     *
     * @param message the message to be processed, represented as a String.
     *                It cannot be null and must follow the expected format or structure
     *                required by the implementation.
     */
    void handle(String message);
}