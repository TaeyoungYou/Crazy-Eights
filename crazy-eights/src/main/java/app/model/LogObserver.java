package app.model;

public interface LogObserver {
    void updateLog(String message, State state);
}
