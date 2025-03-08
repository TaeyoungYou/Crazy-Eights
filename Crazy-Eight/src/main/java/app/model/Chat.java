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
        if(messages.size() < 3) return getLastMessage();
        String message="";
        for(int i = messages.size() - 3; i <messages.size(); i++){
            message += messages.get(i)+" ";
        }
        return message;
    }
    public String getLastMessage(){
        if(messages.isEmpty()) return "Start Game!";
        return messages.getLast();
    }

    private  void notifyMessage(String message){
        controller.updateChat(message, null);
    }

    private void notifyMessage(String message, Player player){
        controller.updateChat(message,player);
    }
}
