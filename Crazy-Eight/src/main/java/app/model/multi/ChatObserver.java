package app.model.multi;

/**
 * An interface representing an observer in the observer design pattern specifically
 * for a chat system. It is intended to notify observers of new chat messages.
 */
public interface ChatObserver {
    /**
     * Updates the chat with a given message and the player who sent it.
     *
     * @param message The message to be added to the chat.
     * @param player  The player sending the message.
     */
    void updateChat(String message, Player player);
}
