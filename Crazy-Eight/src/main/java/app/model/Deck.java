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
        int[] suits = {0,1,2,3};
        int[] ranks = {0,1,2,3,4,5,6,7,8,9,10,11,12};
        for(int suit : suits) {
            for(int rank : ranks) {
                deck.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(deck);
    }

    public Card drawCard() {
        Card card = deck.removeFirst();
        notification();
        return card;
    }
    public void add(Card card) {
        deck.add(card);
        Collections.shuffle(deck);
    }

    public int deckSize(){
        return deck.size();
    }

    private void notification(){
        controller.update();
    }
}
