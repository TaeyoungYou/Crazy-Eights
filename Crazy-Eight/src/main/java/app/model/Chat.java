package app.model;

import app.controller.SinglePlayGameController;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private SinglePlayGameController controller;
    private List<String> messages;

    public Chat(SinglePlayGameController controller) {
        this.controller = controller;
        messages = new ArrayList<>();
    }

    public void addMessage(String message){
        messages.add(message);
        notifyMessage(message);
    }

    public void addMessage(String message, Player player){
        messages.add(message);
        notifyMessage(message, player);
    }
    public String getRecentMessage(){
        return messages.getLast() + " " + messages.get(messages.size()-2) + " " + messages.get(messages.size()-3);
    }
    public String getLastMessage(){
        return messages.getLast();
    }

    private  void notifyMessage(String message){
        controller.updateChat(message, null);
    }

    private void notifyMessage(String message, Player player){
        controller.updateChat(message,player);
    }
}
