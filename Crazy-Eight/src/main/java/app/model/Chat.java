package app.model;

import app.controller.SinglePlayGameController;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chat system that manages messages and notifies the game controller
 * when a new message is added.
 */
public class Chat {
    private SinglePlayGameController controller;
    private List<String> messages;

    /**
     * Constructs a Chat instance by associating it with a game controller.
     *
     * @param controller the game controller used to handle updates related to chat messages
     */
    public Chat(SinglePlayGameController controller) {
        this.controller = controller;
        messages = new ArrayList<>();
    }

    /**
     * Adds a new message to the chat's message list and notifies
     * the game controller about the added message.
     *
     * @param message the message to be added to the chat
     */
    public void addMessage(String message){
        messages.add(message);
        notifyMessage(message);
    }

    /**
     * Adds a message to the chat and notifies the game controller about the new message,
     * optionally associating the message with a specific player.
     *
     * @param message the message to be added to the chat
     * @param player the player associated with the message
     */
    public void addMessage(String message, Player player){
        messages.add(message);
        notifyMessage(message, player);
    }

    /**
     * Notifies the game controller about a new chat message.
     *
     * @param message the message to be sent to the game controller
     */
    private  void notifyMessage(String message){
        controller.updateChat(message, null);
    }

    /**
     * Notifies the game controller about a new chat message associated with a specific player.
     *
     * @param message the message to be sent to the game controller
     * @param player the player associated with the chat message
     */
    private void notifyMessage(String message, Player player){
        controller.updateChat(message,player);
    }
}
