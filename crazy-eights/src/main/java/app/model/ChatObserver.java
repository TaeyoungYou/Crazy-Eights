package app.model;

public interface ChatObserver {
    void updateChat(String message, Player player);
}
