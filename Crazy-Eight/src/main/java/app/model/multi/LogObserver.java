package app.model.multi;

/**
 * The LogObserver interface defines the contract for objects that observe and
 * respond to log updates. Implementers of this interface receive log messages
 * along with their associated state and can perform appropriate actions upon
 * receiving these updates.
 */
public interface LogObserver {
    /**
     * Updates the log with a given message and its associated state.
     *
     * @param message the log message to be updated
     * @param state the state associated with the log message (e.g., System, Log, Error)
     */
    void updateLog(String message, State state);
}
