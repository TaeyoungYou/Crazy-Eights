package app.model;

import app.controller.SinglePlayGameController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private ArrayList<Card> deck;
    private SinglePlayGameController controller;

    public Deck(SinglePlayGameController controller) {
        this.controller = controller;
        deck = new ArrayList<>();
    }
    public void generateDeck() {
        int[] suits = {0,1};
        int[] ranks = {0,1,2,3,4,5,6,7,8,9,10,11,12};
        for(int suit : suits) {
            for(int rank : ranks) {
                deck.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(deck);
    }

    public Card drawCard() {
        Card card = deck.remove(0);
        notification();
        if(deck.isEmpty()) {
            resettingDeck(controller.emptyDeck());
        }
        System.out.println(deck.size());
        return card;
    }
    private void resettingDeck(List<Player> players){
        deck.clear();
        generateDeck();
        for(Player player : players){
            deck.removeAll(player.getHand());
        }
        controller.resetDeck();
    }

    private void notification(){
        controller.update();
    }
}
