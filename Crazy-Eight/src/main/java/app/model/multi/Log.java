package app.model.multi;

import app.controller.multi.BaseGameController;
import app.controller.multi.GameClientController;
import app.controller.multi.GameController;

import java.util.ArrayList;
import java.util.List;

/**
 * The Log class is responsible for managing a list of log messages and notifying
 * a game controller whenever a new log entry is added. It maintains the log
 * messages and delegates updates to the SinglePlayGameController.
 */
public class Log {
    private List<String> logs = new ArrayList<>();
    private BaseGameController controller;

    /**
     * Constructs a Log instance with the given game controller.
     *
     * @param controller the SinglePlayGameController responsible for handling log updates
     */
    public Log(GameController controller) {
        this.controller = controller;
    }

    public Log(GameClientController controller) {
        this.controller = controller;
    }

    /**
     * Adds a log message to the list of logs and notifies the game controller
     * about the new log entry with the specified message and state.
     *
     * @param message the log message to be added
     * @param state   the state associated with the log message (e.g., System, Log, Error)
     */
    public void setLogs(String message, State state) {
        logs.add(message);
        notifyLog(message, state);
    }


    /**
     * Notifies the game controller about a new log entry with a specified message and state.
     *
     * @param message the log message to be sent to the controller
     * @param state   the state associated with the log message (e.g., System, Log, Error)
     */
    private void notifyLog(String message, State state) {
        controller.updateLog(message, state);
    }
}
