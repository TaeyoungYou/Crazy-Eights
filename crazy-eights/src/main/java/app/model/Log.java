package app.model;

import app.controller.SinglePlayGameController;

import java.util.ArrayList;
import java.util.List;

public class Log {
    private List<String> logs = new ArrayList<>();
    private SinglePlayGameController controller;
    public Log(SinglePlayGameController controller) {
        this.controller = controller;
    }

    public void setLogs(String message, State state) {
        logs.add(message);
        notifyLog(message, state);
    }


    private void notifyLog(String message, State state) {
        controller.updateLog(message, state);
    }
}
